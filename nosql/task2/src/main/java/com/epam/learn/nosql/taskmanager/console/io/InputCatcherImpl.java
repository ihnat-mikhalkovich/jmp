package com.epam.learn.nosql.taskmanager.console.io;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Scanner;

@Service
@RequiredArgsConstructor
public class InputCatcherImpl implements InputCatcher {
    private final Scanner scanner;

    @Override
    public String next() {
        return scanner.next();
    }

    @Override
    public int nextInt() {
        return scanner.nextInt();
    }
}
