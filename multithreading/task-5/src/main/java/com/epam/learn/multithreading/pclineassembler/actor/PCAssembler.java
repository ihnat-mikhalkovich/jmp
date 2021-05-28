package com.epam.learn.multithreading.pclineassembler.actor;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.DispatcherSelector;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import com.epam.learn.multithreading.pclineassembler.DetailNames;
import com.epam.learn.multithreading.pclineassembler.entity.PC;
import com.epam.learn.multithreading.pclineassembler.entity.Peripheral;
import com.epam.learn.multithreading.pclineassembler.util.DetailsExtractor;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PCAssembler extends AbstractBehavior<PCAssembler.Command> {

    private final ActorRef<EnclosureAssembler.Command> enclosureAssembler;

    private PCAssembler(ActorContext<Command> context) {
        super(context);
        enclosureAssembler = context.spawn(EnclosureAssembler.create(), "enclosureAssembler", DispatcherSelector.sameAsParent());
    }

    public static Behavior<Command> create() {
        return Behaviors.setup(PCAssembler::new);
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(StartPcAssemble.class, this::onStartPcAssemble)
                .onMessage(CompletePcAssemble.class, this::onCompletePcAssemble)
                .build();
    }

    private Behavior<Command> onStartPcAssemble(StartPcAssemble startPcAssemble) {
        getContext().getLog().debug("Make sure the parts match");

        final Map<String, String> enclosureDetails
                = DetailsExtractor.extractDetails(startPcAssemble.startAssembleLine.details, DetailNames.
                ENCLOSURE_ID, DetailNames.MOTHERBOARD_ID, DetailNames.CPU_ID);

        enclosureAssembler.tell(
                new EnclosureAssembler.StartEnclosureAssemble(startPcAssemble, enclosureDetails, getContext().getSelf())
        );
        return this;
    }

    private Behavior<Command> onCompletePcAssemble(CompletePcAssemble completePcAssemble) {
        final PC pc = new PC();

        pc.setName(completePcAssemble.startPcAssemble.startAssembleLine.details.get(DetailNames.PC_NAME));

        final List<Peripheral> peripherals = Arrays.stream(completePcAssemble.startPcAssemble.startAssembleLine.details
                .get(DetailNames.PC_PLUGGED_IN_PERIPHERAL)
                .split(" "))
                .map(Peripheral::valueOf)
                .collect(Collectors.toList());

        getContext().getLog().debug("Plugging peripherals");
        pc.setPluggedInPeripherals(peripherals);
        getContext().getLog().debug("Firmware tweaks");
        getContext().getLog().debug("Installing the OS and drivers");
        pc.setOs(completePcAssemble.startPcAssemble.startAssembleLine.details.get(DetailNames.PC_OS));

        completePcAssemble.startPcAssemble.pcDataActorRef
                .tell(new PcDataActor.CompletePC(pc, completePcAssemble.startPcAssemble.startAssembleLine.replyTo));

        return this;
    }

    public interface Command {
    }

    public static final class StartPcAssemble implements PCAssembler.Command {
        public final ActorRef<PcDataActor.Command> pcDataActorRef;
        public final AssemblyLine.StartAssembleLine startAssembleLine;
        public final ActorRef<AssemblyLine.Command> replyTo;

        public StartPcAssemble(ActorRef<PcDataActor.Command> pcDataActorRef, AssemblyLine.StartAssembleLine startAssembleLine, ActorRef<AssemblyLine.Command> replyTo) {
            this.pcDataActorRef = pcDataActorRef;
            this.startAssembleLine = startAssembleLine;
            this.replyTo = replyTo;
        }
    }

    public static final class CompletePcAssemble implements PCAssembler.Command {
        public final StartPcAssemble startPcAssemble;

        public CompletePcAssemble(StartPcAssemble startPcAssemble) {
            this.startPcAssemble = startPcAssemble;
        }
    }
}
