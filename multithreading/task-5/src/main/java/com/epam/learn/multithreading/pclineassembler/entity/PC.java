package com.epam.learn.multithreading.pclineassembler.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PC {
    private String name;
    private Enclosure enclosure = new Enclosure();
    private List<Peripheral> pluggedInPeripherals = new ArrayList<>();
    private String os;

    public PC(String name) {
        this.name = name;
    }
}
