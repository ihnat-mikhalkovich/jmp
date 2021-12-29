package com.epam.learn.nosql.taskmanager.console.action.impl;

import com.epam.learn.nosql.taskmanager.console.action.AbstractTaskConsoleAction;
import com.epam.learn.nosql.taskmanager.console.io.InputCatcher;
import com.epam.learn.nosql.taskmanager.console.io.OutputPitcher;
import com.epam.learn.nosql.taskmanager.entity.Task;
import com.epam.learn.nosql.taskmanager.service.TaskService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShowOverdueTasksAction extends AbstractTaskConsoleAction {

    private static final String DEFAULT_DESCRIPTION = "Show overdue tasks";

    public ShowOverdueTasksAction(InputCatcher inputCatcher, OutputPitcher outputPitcher, TaskService taskService) {
        super(inputCatcher, outputPitcher, taskService, DEFAULT_DESCRIPTION);
    }

    @Override
    public boolean act() {
        final List<Task> tasks = taskService.overdueTasks();
        outputPitcher.send(tasks);
        return true;
    }

    @Override
    public int getActionNumber() {
        return 2;
    }
}
