package com.epam.learn.nosql.taskmanager.console.action.impl;

import com.epam.learn.nosql.taskmanager.console.action.AbstractTaskConsoleAction;
import com.epam.learn.nosql.taskmanager.console.io.InputCatcher;
import com.epam.learn.nosql.taskmanager.console.io.OutputPitcher;
import com.epam.learn.nosql.taskmanager.entity.Task;
import com.epam.learn.nosql.taskmanager.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FullTextSearchAction extends AbstractTaskConsoleAction {

    private static final String DEFAULT_DESCRIPTION = "Full-text search by word";
    private static final String DEFAULT_CATEGORY_MESSAGE = "Write the word";

    private final String categoryMessage;

    public FullTextSearchAction(InputCatcher inputCatcher, OutputPitcher outputPitcher, TaskService taskService, String categoryMessage) {
        super(inputCatcher, outputPitcher, taskService, DEFAULT_DESCRIPTION);
        this.categoryMessage = categoryMessage;
    }

    @Autowired
    public FullTextSearchAction(InputCatcher inputCatcher, OutputPitcher outputPitcher, TaskService taskService) {
        this(inputCatcher, outputPitcher, taskService, DEFAULT_CATEGORY_MESSAGE);
    }

    @Override
    public boolean act() {
        outputPitcher.send(categoryMessage + ": ");
        final String text = inputCatcher.next();
        final List<Task> tasks = taskService.fullTextSearch(text);
        outputPitcher.send(tasks);
        return true;
    }

    @Override
    public int getActionNumber() {
        return 10;
    }
}
