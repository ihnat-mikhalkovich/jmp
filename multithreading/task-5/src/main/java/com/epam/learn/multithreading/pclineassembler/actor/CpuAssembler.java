package com.epam.learn.multithreading.pclineassembler.actor;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import com.epam.learn.multithreading.pclineassembler.DetailNames;
import com.epam.learn.multithreading.pclineassembler.entity.Cpu;

import java.util.Map;

public class CpuAssembler extends AbstractBehavior<CpuAssembler.Command> {

    private CpuAssembler(ActorContext<Command> context) {
        super(context);
    }

    public static Behavior<Command> create() {
        return Behaviors.setup(CpuAssembler::new);
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(StartCpuAssemble.class, this::onStartCpuAssemble)
                .build();
    }

    private Behavior<Command> onStartCpuAssemble(StartCpuAssemble startCpuAssemble) {
        final Cpu cpu = new Cpu();
        cpu.setName(startCpuAssemble.cpuDetails.get(DetailNames.CPU_NAME));
        getContext().getLog().debug("Priming the processor");
        cpu.setProcessor(startCpuAssemble.cpuDetails.get(DetailNames.CPU_PROCESSOR));
        getContext().getLog().debug("Affixing the cooler");
        cpu.setCooler(startCpuAssemble.cpuDetails.get(DetailNames.CPU_COOLER));

        startCpuAssemble.replyTo.tell(new MotherBoardAssembler.InsertCpu(startCpuAssemble.startMotherboardAssemble, cpu));
        return this;
    }

    public interface Command {
    }

    public static final class StartCpuAssemble implements CpuAssembler.Command {
        public final MotherBoardAssembler.StartMotherboardAssemble startMotherboardAssemble;
        public final Map<String, String> cpuDetails;
        public final ActorRef<MotherBoardAssembler.Command> replyTo;

        public StartCpuAssemble(MotherBoardAssembler.StartMotherboardAssemble startMotherboardAssemble, Map<String, String> cpuDetails, ActorRef<MotherBoardAssembler.Command> replyTo) {
            this.startMotherboardAssemble = startMotherboardAssemble;
            this.cpuDetails = cpuDetails;
            this.replyTo = replyTo;
        }
    }
}
