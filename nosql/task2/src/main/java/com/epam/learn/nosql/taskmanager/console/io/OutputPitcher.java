package com.epam.learn.nosql.taskmanager.console.io;

public interface OutputPitcher {
    void send(String message);
    void send(Object object);
}
