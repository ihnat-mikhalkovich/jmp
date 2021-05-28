package com.epam.learn.multithreading.pclineassembler.service;

import akka.actor.typed.ActorSystem;
import akka.actor.typed.javadsl.AskPattern;
import com.epam.learn.multithreading.pclineassembler.actor.AssemblyLine;
import com.epam.learn.multithreading.pclineassembler.entity.PC;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.CompletionStage;

@RequiredArgsConstructor
public class PcServiceImpl implements PcService {
    private final ActorSystem<AssemblyLine.Command> assemblyLine;

    @Override
    public CompletionStage<PC> createPc(Map<String, String> details) {
        final CompletionStage<PC> ask = AskPattern.ask(
                assemblyLine,
                replyTo -> new AssemblyLine.StartAssembleLine(details, replyTo),
                Duration.ofSeconds(50),
                assemblyLine.scheduler()
        );

        return ask;
    }
}
