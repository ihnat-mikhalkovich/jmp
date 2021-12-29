package com.epam.learn.nosql.taskmanager.console.action.impl;

import com.epam.learn.nosql.taskmanager.console.action.AbstractTaskConsoleAction;
import com.epam.learn.nosql.taskmanager.console.io.InputCatcher;
import com.epam.learn.nosql.taskmanager.console.io.OutputPitcher;
import com.epam.learn.nosql.taskmanager.entity.Subtask;
import com.epam.learn.nosql.taskmanager.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShowAllSubtasksOfTasksWithSpecificCategory extends AbstractTaskConsoleAction {

    private static final String DEFAULT_DESCRIPTION = "Show all subtasks of tasks with specific category";
    private static final String DEFAULT_CATEGORY_MESSAGE = "Give the category name please";

    private final String categoryMessage;

    public ShowAllSubtasksOfTasksWithSpecificCategory(InputCatcher inputCatcher, OutputPitcher outputPitcher, TaskService taskService, String categoryMessage) {
        super(inputCatcher, outputPitcher, taskService, DEFAULT_DESCRIPTION);
        this.categoryMessage = categoryMessage;
    }

    @Autowired
    public ShowAllSubtasksOfTasksWithSpecificCategory(InputCatcher inputCatcher, OutputPitcher outputPitcher, TaskService taskService) {
        this(inputCatcher, outputPitcher, taskService, DEFAULT_CATEGORY_MESSAGE);
    }

    @Override
    public boolean act() {
        outputPitcher.send(categoryMessage + ": ");
        final String category = inputCatcher.next();
        final List<Subtask> result = taskService.findAllSubtasksOfTasksOfCategory(category);
        outputPitcher.send(result);
        return true;
    }

    @Override
    public int getActionNumber() {
        return 4;
    }
}
