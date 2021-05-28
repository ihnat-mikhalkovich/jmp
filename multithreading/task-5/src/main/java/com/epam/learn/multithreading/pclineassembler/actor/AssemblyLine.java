package com.epam.learn.multithreading.pclineassembler.actor;

import akka.actor.typed.*;
import akka.actor.typed.javadsl.*;
import com.epam.learn.multithreading.pclineassembler.entity.PC;

import java.util.Map;
import java.util.UUID;

public class AssemblyLine extends AbstractBehavior<AssemblyLine.Command> {

    private final ActorRef<PCAssembler.Command> pcAssembler;

    private AssemblyLine(ActorContext<Command> context) {
        super(context);
        pcAssembler = context.spawn(PCAssembler.create(), "pcAssembler", DispatcherSelector.sameAsParent());
    }

    public static Behavior<Command> create() {
        return Behaviors.setup(AssemblyLine::new);
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(StartAssembleLine.class, this::onStartAssemblyLine)
                .onMessage(EndAssembleLine.class, this::onEndAssembleLine)
                .build();
    }

    private Behavior<Command> onStartAssemblyLine(StartAssembleLine startAssembleLine) {
        getContext().getLog().debug("Setting the stage");

        final ActorRef<PcDataActor.Command> pcDataActorRef
                = getContext().spawn(PcDataActor.create(getContext().getSelf()), "pc-" + UUID.randomUUID(), DispatcherSelector.sameAsParent());

        pcAssembler.tell(new PCAssembler.StartPcAssemble(pcDataActorRef, startAssembleLine, getContext().getSelf()));
        return this;
    }

    private Behavior<Command> onEndAssembleLine(EndAssembleLine endAssembleLine) {
        getContext().getLog().debug("Result pc: {}", endAssembleLine.pc);
        endAssembleLine.replyTo.tell(endAssembleLine.pc);
        return this;
    }

    public interface Command {
    }

    public static final class StartAssembleLine implements Command {
        public final Map<String, String> details;
        public final ActorRef<PC> replyTo;

        public StartAssembleLine(Map<String, String> details, ActorRef<PC> replyTo) {
            this.details = details;
            this.replyTo = replyTo;
        }
    }

    public static final class EndAssembleLine implements Command {
        public final PC pc;
        public final ActorRef<PcDataActor.Command> pcDataActorRef;
        public final ActorRef<PC> replyTo;

        public EndAssembleLine(PC pc, ActorRef<PcDataActor.Command> pcDataActorRef, ActorRef<PC> replyTo) {
            this.pc = pc;
            this.pcDataActorRef = pcDataActorRef;
            this.replyTo = replyTo;
        }
    }

    public static Behavior<Command> createPool(int poolSize, Props routeeProps) {
        return
                Behaviors.setup(
                        context -> {
                            PoolRouter<Command> pool =
                                    Routers.pool(
                                            poolSize,
                                            Behaviors.supervise(AssemblyLine.create()).onFailure(SupervisorStrategy.restart()));

                            return pool.withRouteeProps(routeeProps);
                        });
    }
}
