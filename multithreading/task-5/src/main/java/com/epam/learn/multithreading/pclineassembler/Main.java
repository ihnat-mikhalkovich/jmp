package com.epam.learn.multithreading.pclineassembler;

import akka.actor.typed.ActorSystem;
import akka.actor.typed.DispatcherSelector;
import com.epam.learn.multithreading.pclineassembler.actor.AssemblyLine;
import com.epam.learn.multithreading.pclineassembler.service.PcServiceImpl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalLong;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.epam.learn.multithreading.pclineassembler.DetailNames.*;
import static com.epam.learn.multithreading.pclineassembler.entity.Peripheral.*;

public class Main {

    public static void main(String[] args) {
        final ActorSystem<AssemblyLine.Command> assemblyLine =
                ActorSystem.create(
                        AssemblyLine.createPool(100, DispatcherSelector.fromConfig("fork-join-dispatcher")),
                        "assemblyLine");

        final PcServiceImpl pcService = new PcServiceImpl(assemblyLine);

        final Map<String, String> details = buildDetails();

        final int pcAmount = 1_00_000;

        final List<Map<String, String>> detailsList = Stream.iterate(0, i -> ++i).limit(pcAmount).map(i -> {
            return new HashMap<>(details).entrySet().stream()
                    .map(entry -> {
                        if (PC_PLUGGED_IN_PERIPHERAL.equalsIgnoreCase(entry.getKey())) {
                            return entry;
                        }
                        final String newValue = entry.getValue() + "-" + i;
                        entry.setValue(newValue);
                        return entry;
                    })
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }).collect(Collectors.toList());

        final long start = System.currentTimeMillis();
        final OptionalLong max = detailsList.stream()
                .mapToLong(newDetails -> {
                    try {
                        return pcService.createPc(newDetails)
                                .thenApply(pc -> System.currentTimeMillis()).toCompletableFuture().get() - start;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    return 0;
                }).max();
        final long end = System.currentTimeMillis();
        assemblyLine.log().info("Longest calculation: {}", max);
        assemblyLine.log().info("Duration: {}", end - start);


        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            assemblyLine.terminate();
        }
    }

    public static Map<String, String> buildDetails() {
        final Map<String, String> details = new HashMap<>();

        details.put(CPU_NAME, "cpu-name-1");
        details.put(CPU_COOLER, "cpu-cooler-1");
        details.put(CPU_PROCESSOR, "m1");

        details.put(PC_NAME, "Rocket");
        details.put(PC_PLUGGED_IN_PERIPHERAL, MOUSE
                + " " + KEYBOARD
                + " " + MONITOR
                + " " + SPEAKERS);
        details.put(PC_OS, "osX");

        details.put(ENCLOSURE_NAME, "enclosure.name-1");
        details.put(ENCLOSURE_SSD, "enclosure.ssd-1");
        details.put(ENCLOSURE_OPTICAL_DRIVE, "enclosure.opticalDrive-1");
        details.put(ENCLOSURE_POWER, "enclosure.moarPower-1");
        details.put(ENCLOSURE_IS_CABLES_CONNECTED, "enclosure.isCablesConnected-1");
        details.put(ENCLOSURE_LIQUID_COOLING, "enclosure.liquidCooling-1");

        details.put(MOTHERBOARD_NAME, "motherBoard.name-1");
        details.put(MOTHERBOARD_RAM_MEMORY, "motherBoard.ramMemory-1");
        details.put(MOTHERBOARD_GRAPHICS, "motherBoard.graphics-1");
        details.put(MOTHERBOARD_LAN_CARD, "motherBoard.lanCar-1");


        return details;
    }
}
