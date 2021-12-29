package com.epam.learn.nosql.taskmanager.console.action.impl;

import com.epam.learn.nosql.taskmanager.console.action.AbstractTaskConsoleAction;
import com.epam.learn.nosql.taskmanager.console.io.InputCatcher;
import com.epam.learn.nosql.taskmanager.console.io.OutputPitcher;
import com.epam.learn.nosql.taskmanager.entity.Subtask;
import com.epam.learn.nosql.taskmanager.entity.Task;
import com.epam.learn.nosql.taskmanager.service.TaskService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddSubtaskAction extends AbstractTaskConsoleAction {

    private static final String DEFAULT_DESCRIPTION = "Add subtask task";
    private static final String DEFAULT_CATEGORY_MESSAGE = "Put json next";

    private final String categoryMessage;
    private final ObjectMapper objectMapper;

    public AddSubtaskAction(InputCatcher inputCatcher, OutputPitcher outputPitcher, TaskService taskService, ObjectMapper objectMapper, String categoryMessage) {
        super(inputCatcher, outputPitcher, taskService, DEFAULT_DESCRIPTION);
        this.objectMapper = objectMapper;
        this.categoryMessage = categoryMessage;
    }

    @Autowired
    public AddSubtaskAction(InputCatcher inputCatcher, OutputPitcher outputPitcher, TaskService taskService, ObjectMapper objectMapper) {
        this(inputCatcher, outputPitcher, taskService, objectMapper, DEFAULT_CATEGORY_MESSAGE);
    }

    @Override
    public boolean act() {
        outputPitcher.send(categoryMessage + ": ");
        final String taskJson = inputCatcher.next();

        try {
            final Task task = objectMapper.readValue(taskJson, Task.class);
            final List<Subtask> subtasks = taskService.createSubtasks(task);
            outputPitcher.send(subtasks);
        } catch (JsonProcessingException e) {
            outputPitcher.send("Bad json");
        }

        return true;
    }

    @Override
    public int getActionNumber() {
        return 8;
    }
}
