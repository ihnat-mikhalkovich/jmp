package com.epam.learn.nosql.taskmanager;

import com.epam.learn.nosql.taskmanager.console.interactor.ConsoleInteractor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
public class Main implements CommandLineRunner {

    private final ConsoleInteractor consoleInteractor;

    public static void main(String[] args) {
        log.info("Task manager has started.");
        SpringApplication.run(Main.class, args);
        log.info("Task manager finished.");
    }

    @Override
    public void run(String... args) {
        consoleInteractor.start();
    }
}
