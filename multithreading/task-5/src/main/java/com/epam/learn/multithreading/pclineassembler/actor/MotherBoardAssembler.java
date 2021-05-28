package com.epam.learn.multithreading.pclineassembler.actor;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.DispatcherSelector;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import com.epam.learn.multithreading.pclineassembler.DetailNames;
import com.epam.learn.multithreading.pclineassembler.entity.Cpu;
import com.epam.learn.multithreading.pclineassembler.entity.MotherBoard;
import com.epam.learn.multithreading.pclineassembler.util.DetailsExtractor;

import java.util.Map;

public class MotherBoardAssembler extends AbstractBehavior<MotherBoardAssembler.Command> {

    private final ActorRef<CpuAssembler.Command> cpuAssembler;

    private MotherBoardAssembler(ActorContext<Command> context) {
        super(context);
        cpuAssembler = context.spawn(CpuAssembler.create(), "cpuAssembler", DispatcherSelector.sameAsParent());
    }

    public static Behavior<Command> create() {
        return Behaviors.setup(MotherBoardAssembler::new);
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(StartMotherboardAssemble.class, this::onStartMotherboardAssemble)
                .onMessage(InsertCpu.class, this::onInsertCpu)
                .build();
    }

    private Behavior<Command> onStartMotherboardAssemble(StartMotherboardAssemble startMotherboardAssemble) {
        final Map<String, String> cpuDetails
                = DetailsExtractor.extractDetails(startMotherboardAssemble.motherboardDetails, DetailNames.CPU_ID);
        cpuAssembler.tell(new CpuAssembler.StartCpuAssemble(startMotherboardAssemble, cpuDetails, getContext().getSelf()));

        final MotherBoard motherBoard = new MotherBoard();
        motherBoard.setName(startMotherboardAssemble.motherboardDetails.get(DetailNames.MOTHERBOARD_NAME));
        getContext().getLog().debug("Seating the memory");
        motherBoard.setRamMemory(startMotherboardAssemble.motherboardDetails.get(DetailNames.MOTHERBOARD_RAM_MEMORY));

        startMotherboardAssemble.startEnclosureAssemble
                .startPcAssemble.pcDataActorRef.tell(new PcDataActor.InsertMotherboard(motherBoard, startMotherboardAssemble));

        return this;
    }

    private Behavior<Command> onInsertCpu(InsertCpu insertCpu) {
        getContext().getLog().debug("Installing the CPU");
        insertCpu.startMotherboardAssemble.startEnclosureAssemble.startPcAssemble
                .pcDataActorRef.tell(new PcDataActor.InsertCpu(insertCpu.cpu,
                    insertCpu.startMotherboardAssemble));
        return this;
    }

    public interface Command {
    }

    public static final class StartMotherboardAssemble implements Command {
        public final EnclosureAssembler.StartEnclosureAssemble startEnclosureAssemble;
        public final Map<String, String> motherboardDetails;
        public final ActorRef<EnclosureAssembler.Command> replyTo;

        public StartMotherboardAssemble(EnclosureAssembler.StartEnclosureAssemble startEnclosureAssemble, Map<String, String> motherboardDetails, ActorRef<EnclosureAssembler.Command> replyTo) {
            this.startEnclosureAssemble = startEnclosureAssemble;
            this.motherboardDetails = motherboardDetails;
            this.replyTo = replyTo;
        }
    }

    public static final class InsertCpu implements Command {
        public final MotherBoardAssembler.StartMotherboardAssemble startMotherboardAssemble;
        public final Cpu cpu;

        public InsertCpu(MotherBoardAssembler.StartMotherboardAssemble startMotherboardAssemble, Cpu cpu) {
            this.startMotherboardAssemble = startMotherboardAssemble;
            this.cpu = cpu;
        }
    }
}
