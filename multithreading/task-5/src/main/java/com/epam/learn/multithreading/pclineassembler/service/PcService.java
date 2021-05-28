package com.epam.learn.multithreading.pclineassembler.service;

import com.epam.learn.multithreading.pclineassembler.entity.PC;

import java.util.Map;
import java.util.concurrent.CompletionStage;

public interface PcService {
    CompletionStage<PC> createPc(Map<String, String> details);
}
