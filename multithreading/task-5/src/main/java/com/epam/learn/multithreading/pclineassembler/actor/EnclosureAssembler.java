package com.epam.learn.multithreading.pclineassembler.actor;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.DispatcherSelector;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import com.epam.learn.multithreading.pclineassembler.DetailNames;
import com.epam.learn.multithreading.pclineassembler.entity.Enclosure;
import com.epam.learn.multithreading.pclineassembler.entity.MotherBoard;
import com.epam.learn.multithreading.pclineassembler.util.DetailsExtractor;

import java.util.Map;

public class EnclosureAssembler extends AbstractBehavior<EnclosureAssembler.Command> {

    private final ActorRef<MotherBoardAssembler.Command> motherboardAssembler;

    private EnclosureAssembler(ActorContext<Command> context) {
        super(context);
        motherboardAssembler = context.spawn(MotherBoardAssembler.create(), "motherboardAssembler", DispatcherSelector.sameAsParent());
    }

    public static Behavior<Command> create() {
        return Behaviors.setup(EnclosureAssembler::new);
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(StartEnclosureAssemble.class, this::onStartEnclosureAssemble)
                .onMessage(EndEnclosureAssemble.class, this::onEndEnclosureAssemble)
                .build();
    }

    private Behavior<Command> onStartEnclosureAssemble(StartEnclosureAssemble startEnclosureAssemble) {
        final Map<String, String> motherboardDetails
                = DetailsExtractor.extractDetails(startEnclosureAssemble.enclosureDetails, DetailNames.MOTHERBOARD_ID, DetailNames.CPU_ID);
        motherboardAssembler.tell(
                new MotherBoardAssembler.StartMotherboardAssemble(startEnclosureAssemble, motherboardDetails, getContext().getSelf())
        );

        getContext().getLog().debug("Preparing the enclosure");
        final Enclosure enclosure = new Enclosure();
        getContext().getLog().debug("Making sense of all the screws");
        enclosure.setName(startEnclosureAssemble.enclosureDetails.get(DetailNames.ENCLOSURE_NAME));
        getContext().getLog().debug("Hard drive and SSD installation");
        enclosure.setSsd(startEnclosureAssemble.enclosureDetails.get(DetailNames.ENCLOSURE_SSD));
        getContext().getLog().debug("Adding an optical drive");
        enclosure.setOpticalDrive(startEnclosureAssemble.enclosureDetails.get(DetailNames.ENCLOSURE_OPTICAL_DRIVE));
        getContext().getLog().debug("Swapping a fan");

        startEnclosureAssemble.startPcAssemble.pcDataActorRef.tell(new PcDataActor.InsertEnclosure(enclosure));

        return this;
    }

    private Behavior<Command> onEndEnclosureAssemble(EndEnclosureAssemble endEnclosureAssemble) {
        final Enclosure enclosure = new Enclosure();
        getContext().getLog().debug("Inserting the motherboard");
        enclosure.setMotherBoard(endEnclosureAssemble.motherBoard);
        getContext().getLog().debug("Graphics time");
        enclosure.getMotherBoard().setGraphics(endEnclosureAssemble.enclosureDetails.get(DetailNames.MOTHERBOARD_GRAPHICS));
        getContext().getLog().debug("Another expansion card");
        enclosure.getMotherBoard().setLanCard(endEnclosureAssemble.enclosureDetails.get(DetailNames.MOTHERBOARD_LAN_CARD));
        getContext().getLog().debug("Moar power");
        enclosure.setMoarPower(endEnclosureAssemble.enclosureDetails.get(DetailNames.ENCLOSURE_POWER));
        getContext().getLog().debug("Connecting cables");
        enclosure.setCablesConnected(true);
        getContext().getLog().debug("One more thing: Liquid cooling");
        enclosure.setLiquidCooling(endEnclosureAssemble.enclosureDetails.get(DetailNames.ENCLOSURE_LIQUID_COOLING));

        endEnclosureAssemble.startPcAssemble.pcDataActorRef.tell(new PcDataActor.InsertEnclosure(enclosure));

        endEnclosureAssemble.replyTo.tell(new PCAssembler.CompletePcAssemble(endEnclosureAssemble.startPcAssemble));

        return this;
    }

    public interface Command {
    }

    public static final class StartEnclosureAssemble implements EnclosureAssembler.Command {
        public final PCAssembler.StartPcAssemble startPcAssemble;
        public final Map<String, String> enclosureDetails;
        public final ActorRef<PCAssembler.Command> replyTo;

        public StartEnclosureAssemble(PCAssembler.StartPcAssemble startPcAssemble, Map<String, String> enclosureDetails, ActorRef<PCAssembler.Command> replyTo) {
            this.startPcAssemble = startPcAssemble;
            this.enclosureDetails = enclosureDetails;
            this.replyTo = replyTo;
        }
    }

    public static final class EndEnclosureAssemble implements EnclosureAssembler.Command {
        public final PCAssembler.StartPcAssemble startPcAssemble;
        public final Map<String, String> enclosureDetails;
        public final ActorRef<PCAssembler.Command> replyTo;
        public final MotherBoard motherBoard;

        public EndEnclosureAssemble(PCAssembler.StartPcAssemble startPcAssemble, Map<String, String> enclosureDetails, ActorRef<PCAssembler.Command> replyTo, MotherBoard motherBoard) {
            this.startPcAssemble = startPcAssemble;
            this.enclosureDetails = enclosureDetails;
            this.replyTo = replyTo;
            this.motherBoard = motherBoard;
        }
    }
}
