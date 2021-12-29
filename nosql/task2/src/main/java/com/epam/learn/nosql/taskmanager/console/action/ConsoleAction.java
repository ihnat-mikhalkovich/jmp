package com.epam.learn.nosql.taskmanager.console.action;

import org.springframework.beans.factory.annotation.Autowired;

public interface ConsoleAction {
    String getDescription();
    void setDescription(String actionDescription);
    boolean act();
    int getActionNumber();

    @Autowired
    default void register(ConsoleActionDirector director) {
        director.register(this);
    }
}
