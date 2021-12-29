package com.epam.learn.nosql.taskmanager.console.action.impl;

import com.epam.learn.nosql.taskmanager.console.action.AbstractTaskConsoleAction;
import com.epam.learn.nosql.taskmanager.console.io.InputCatcher;
import com.epam.learn.nosql.taskmanager.console.io.OutputPitcher;
import com.epam.learn.nosql.taskmanager.entity.Task;
import com.epam.learn.nosql.taskmanager.service.TaskService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeleteTaskAction extends AbstractTaskConsoleAction {

    private static final String DEFAULT_DESCRIPTION = "Delete task";
    private static final String DEFAULT_CATEGORY_MESSAGE = "Put Id next";

    private final String categoryMessage;

    public DeleteTaskAction(InputCatcher inputCatcher, OutputPitcher outputPitcher, TaskService taskService, String categoryMessage) {
        super(inputCatcher, outputPitcher, taskService, DEFAULT_DESCRIPTION);
        this.categoryMessage = categoryMessage;
    }

    @Autowired
    public DeleteTaskAction(InputCatcher inputCatcher, OutputPitcher outputPitcher, TaskService taskService) {
        this(inputCatcher, outputPitcher, taskService, DEFAULT_CATEGORY_MESSAGE);
    }

    @Override
    public boolean act() {
        outputPitcher.send(categoryMessage + ": ");
        final String taskId = inputCatcher.next();

        final Task task = new Task();
        task.setId(taskId);
        taskService.deleteTask(task);

        return true;
    }

    @Override
    public int getActionNumber() {
        return 7;
    }
}
