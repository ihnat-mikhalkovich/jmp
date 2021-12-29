package com.epam.learn.nosql.taskmanager.console.action;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

@Service
@RequiredArgsConstructor
public class ConsoleActionDirectorImpl implements ConsoleActionDirector {

    private final Map<Integer, ConsoleAction> idActionMap;

    public ConsoleActionDirectorImpl() {
        idActionMap = new HashMap<>();
    }

    @Override
    public void register(ConsoleAction action) {
        idActionMap.put(action.getActionNumber(), action);
    }

    @Override
    public ConsoleAction getById(int id) {
        return idActionMap.get(id);
    }

    @Override
    public void forEach(BiConsumer<Integer, ConsoleAction> biConsumer) {
        idActionMap.forEach(biConsumer);
    }
}
