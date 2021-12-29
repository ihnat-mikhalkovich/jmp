package com.epam.learn.nosql.taskmanager.console.action.impl;

import com.epam.learn.nosql.taskmanager.console.action.AbstractTaskConsoleAction;
import com.epam.learn.nosql.taskmanager.console.io.InputCatcher;
import com.epam.learn.nosql.taskmanager.console.io.OutputPitcher;
import com.epam.learn.nosql.taskmanager.service.TaskService;
import org.springframework.stereotype.Service;

@Service
public class EndConsoleInteractionAction extends AbstractTaskConsoleAction {
    public EndConsoleInteractionAction(InputCatcher inputCatcher, OutputPitcher outputPitcher, TaskService taskService) {
        super(inputCatcher, outputPitcher, taskService, "Exit");
    }

    @Override
    public boolean act() {
        return false;
    }

    @Override
    public int getActionNumber() {
        return 11;
    }
}
