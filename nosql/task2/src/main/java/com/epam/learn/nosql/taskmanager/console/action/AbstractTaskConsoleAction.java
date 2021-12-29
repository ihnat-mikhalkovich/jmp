package com.epam.learn.nosql.taskmanager.console.action;

import com.epam.learn.nosql.taskmanager.console.io.InputCatcher;
import com.epam.learn.nosql.taskmanager.console.io.OutputPitcher;
import com.epam.learn.nosql.taskmanager.service.TaskService;

import java.util.function.Supplier;

public abstract class AbstractTaskConsoleAction implements ConsoleAction {
    protected final InputCatcher inputCatcher;
    protected final OutputPitcher outputPitcher;
    protected final TaskService taskService;

    public AbstractTaskConsoleAction(InputCatcher inputCatcher, OutputPitcher outputPitcher, TaskService taskService, String description) {
        this.inputCatcher = inputCatcher;
        this.outputPitcher = outputPitcher;
        this.taskService = taskService;
        setDescription(description);
    }

    private Supplier<String> descriptionSupplier;

    public String getDescription() {
        return descriptionSupplier.get();
    }

    public void setDescription(String description) {
        descriptionSupplier = () -> description;
    }
}
