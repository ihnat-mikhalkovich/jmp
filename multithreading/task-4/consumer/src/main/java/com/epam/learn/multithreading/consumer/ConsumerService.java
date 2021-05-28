package com.epam.learn.multithreading.consumer;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ConsumerService {
    CompletableFuture<List<Employee>> hiredEmployees();

    CompletableFuture<Double> getSalary(long id);
}
