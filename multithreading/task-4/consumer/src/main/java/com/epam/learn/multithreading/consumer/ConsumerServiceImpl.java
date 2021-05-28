package com.epam.learn.multithreading.consumer;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class ConsumerServiceImpl implements ConsumerService {

    private final RestTemplate client;
    public static String EMPLOYEES_URL = "http://localhost:8080/employees/";

    public static String GET_SALARY_TEMPLATE = EMPLOYEES_URL + "{id}/salary/";

    private final UriComponents getSalaryUri = UriComponentsBuilder.fromUriString(GET_SALARY_TEMPLATE).build();

    @Override
    public CompletableFuture<List<Employee>> hiredEmployees() {
        return CompletableFuture.supplyAsync(() -> client.getForEntity(EMPLOYEES_URL, Employee[].class))
                .thenApply(ResponseEntity::getBody)
                .thenApply(Arrays::asList);
    }

    @Override
    public CompletableFuture<Double> getSalary(long id) {
        final UriComponents userSalaryUri = getSalaryUri.expand(Collections.singletonMap("id", id));
        return CompletableFuture.supplyAsync(() -> client.getForEntity(userSalaryUri.toString(), Double.class))
                .thenApply(ResponseEntity::getBody);
    }

}
