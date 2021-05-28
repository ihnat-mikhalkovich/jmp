package com.epam.learn.multithreading.pclineassembler.actor;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import com.epam.learn.multithreading.pclineassembler.entity.Cpu;
import com.epam.learn.multithreading.pclineassembler.entity.Enclosure;
import com.epam.learn.multithreading.pclineassembler.entity.MotherBoard;
import com.epam.learn.multithreading.pclineassembler.entity.PC;

public class PcDataActor extends AbstractBehavior<PcDataActor.Command> {

    private final PC pc;
    private final ActorRef<AssemblyLine.Command> requester;

    private PcDataActor(ActorContext<Command> context, ActorRef<AssemblyLine.Command> requester) {
        super(context);
        pc = new PC();
        this.requester = requester;
    }

    public static Behavior<PcDataActor.Command> create(ActorRef<AssemblyLine.Command> requester) {
        return Behaviors.setup(context -> new PcDataActor(context, requester));
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(InsertEnclosure.class, this::onInsertEnclosure)
                .onMessage(InsertMotherboard.class, this::onInsertMotherboard)
                .onMessage(InsertCpu.class, this::onInsertCpu)
                .onMessage(CompletePC.class, this::onCompletePC)
                .build();
    }

    private Behavior<Command> onInsertEnclosure(InsertEnclosure insertEnclosure) {
        final Enclosure enclosure = insertEnclosure.enclosure;
        if (pc.getEnclosure().getName() == null) {
            pc.getEnclosure().setName(enclosure.getName());
            pc.getEnclosure().setSsd(enclosure.getSsd());
            pc.getEnclosure().setOpticalDrive(enclosure.getOpticalDrive());
        } else {
            pc.getEnclosure().setMotherBoard(enclosure.getMotherBoard());
            pc.getEnclosure().setMoarPower(enclosure.getMoarPower());
            pc.getEnclosure().setCablesConnected(enclosure.isCablesConnected());
            pc.getEnclosure().setLiquidCooling(enclosure.getLiquidCooling());
        }
        return this;
    }

    private Behavior<Command> onInsertMotherboard(InsertMotherboard insertMotherboard) {
        pc.getEnclosure().getMotherBoard().setName(insertMotherboard.motherBoard.getName());
        pc.getEnclosure().getMotherBoard().setRamMemory(insertMotherboard.motherBoard.getRamMemory());
        if (pc.getEnclosure().getMotherBoard().getCpu() != null) {
            insertMotherboard.startMotherboardAssemble.replyTo
                    .tell(new EnclosureAssembler.EndEnclosureAssemble(
                            insertMotherboard.startMotherboardAssemble.startEnclosureAssemble.startPcAssemble,
                            insertMotherboard.startMotherboardAssemble.startEnclosureAssemble.enclosureDetails,
                            insertMotherboard.startMotherboardAssemble.startEnclosureAssemble.replyTo,
                            new MotherBoard(pc.getEnclosure().getMotherBoard())
                    ));
        }
        return this;
    }

    private Behavior<Command> onInsertCpu(InsertCpu insertCpu) {
        pc.getEnclosure().getMotherBoard().setCpu(insertCpu.cpu);
        if (pc.getEnclosure().getMotherBoard().getRamMemory() != null) {
            insertCpu.startMotherboardAssemble.replyTo
                    .tell(new EnclosureAssembler.EndEnclosureAssemble(
                            insertCpu.startMotherboardAssemble.startEnclosureAssemble.startPcAssemble,
                            insertCpu.startMotherboardAssemble.startEnclosureAssemble.enclosureDetails,
                            insertCpu.startMotherboardAssemble.startEnclosureAssemble.replyTo,
                            new MotherBoard(pc.getEnclosure().getMotherBoard())
                    ));
        }
        return this;
    }

    private Behavior<Command> onCompletePC(CompletePC completePC) {
        pc.setName(completePC.pc.getName());
        pc.setPluggedInPeripherals(completePC.pc.getPluggedInPeripherals());
        pc.setOs(completePC.pc.getOs());
        requester.tell(new AssemblyLine.EndAssembleLine(pc, getContext().getSelf(), completePC.replyTo));
        return this;
    }

    public interface Command {
    }

    public static final class InsertEnclosure implements Command {
        public final Enclosure enclosure;

        public InsertEnclosure(Enclosure enclosure) {
            this.enclosure = enclosure;
        }
    }

    public static final class InsertMotherboard implements Command {
        public final MotherBoard motherBoard;
        public final MotherBoardAssembler.StartMotherboardAssemble startMotherboardAssemble;

        public InsertMotherboard(MotherBoard motherBoard, MotherBoardAssembler.StartMotherboardAssemble startMotherboardAssemble) {
            this.motherBoard = motherBoard;
            this.startMotherboardAssemble = startMotherboardAssemble;
        }
    }

    public static final class InsertCpu implements Command {
        public final Cpu cpu;
        public final MotherBoardAssembler.StartMotherboardAssemble startMotherboardAssemble;

        public InsertCpu(Cpu cpu, MotherBoardAssembler.StartMotherboardAssemble startMotherboardAssemble) {
            this.cpu = cpu;
            this.startMotherboardAssemble = startMotherboardAssemble;
        }
    }

    public static final class CompletePC implements Command {
        public final PC pc;
        public final ActorRef<PC> replyTo;

        public CompletePC(PC pc, ActorRef<PC> replyTo) {
            this.pc = pc;
            this.replyTo = replyTo;
        }
    }
}
