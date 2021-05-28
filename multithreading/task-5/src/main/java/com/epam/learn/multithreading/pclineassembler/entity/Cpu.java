package com.epam.learn.multithreading.pclineassembler.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cpu implements Cloneable {
    private String name;
    private String cooler;
    private String processor;

    public Cpu(Cpu cpu) {
        name = cpu.name;
        cooler = cpu.cooler;
        processor = cpu.processor;
    }
}
