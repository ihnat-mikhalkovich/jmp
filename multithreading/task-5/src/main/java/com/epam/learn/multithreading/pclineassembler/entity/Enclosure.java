package com.epam.learn.multithreading.pclineassembler.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Enclosure {
    private String name;
    private String ssd;
    private String opticalDrive;
    private MotherBoard motherBoard = new MotherBoard();
    private String moarPower;
    private boolean isCablesConnected;
    private String liquidCooling;
}
