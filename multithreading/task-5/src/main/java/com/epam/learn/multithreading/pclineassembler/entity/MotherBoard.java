package com.epam.learn.multithreading.pclineassembler.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MotherBoard {
    private String name;
    private Cpu cpu;
    private String ramMemory;
    private String graphics;
    private String lanCard;

    public MotherBoard(MotherBoard motherBoard) {
        name = motherBoard.name;
        cpu = new Cpu(motherBoard.cpu);
        ramMemory = motherBoard.ramMemory;
        graphics = motherBoard.graphics;
        lanCard = motherBoard.lanCard;
    }
}
