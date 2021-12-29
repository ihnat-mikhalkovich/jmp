package com.epam.learn.nosql.taskmanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Scanner;

@Configuration
public class ConsoleActionConfig {
    @Bean
    public Scanner scanner() {
        final Scanner scanner = new Scanner(System.in);
        return scanner.useDelimiter("\n");
    }
}
