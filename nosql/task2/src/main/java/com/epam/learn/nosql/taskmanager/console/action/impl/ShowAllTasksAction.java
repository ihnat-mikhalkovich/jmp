package com.epam.learn.nosql.taskmanager.console.action.impl;

import com.epam.learn.nosql.taskmanager.console.action.AbstractTaskConsoleAction;
import com.epam.learn.nosql.taskmanager.console.io.InputCatcher;
import com.epam.learn.nosql.taskmanager.console.io.OutputPitcher;
import com.epam.learn.nosql.taskmanager.entity.Task;
import com.epam.learn.nosql.taskmanager.service.TaskService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShowAllTasksAction extends AbstractTaskConsoleAction {

    private static final String DEFAULT_DESCRIPTION = "Show all tasks";

    public ShowAllTasksAction(InputCatcher inputCatcher, OutputPitcher outputPitcher, TaskService taskService) {
        super(inputCatcher, outputPitcher, taskService, DEFAULT_DESCRIPTION);
    }

    @Override
    public boolean act() {
        final List<Task> all = taskService.findAll();
        outputPitcher.send(all);
        return true;
    }

    @Override
    public int getActionNumber() {
        return 1;
    }
}
