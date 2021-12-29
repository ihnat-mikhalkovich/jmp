package com.epam.learn.nosql.taskmanager.console.action;

import java.util.function.BiConsumer;

public interface ConsoleActionDirector {
    void register(ConsoleAction action);
    ConsoleAction getById(int id);

    void forEach(BiConsumer<Integer, ConsoleAction> biConsumer);
}
