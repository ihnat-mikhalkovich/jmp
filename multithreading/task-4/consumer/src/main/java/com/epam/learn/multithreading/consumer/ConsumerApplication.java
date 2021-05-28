package com.epam.learn.multithreading.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@SpringBootApplication
@Slf4j
public class ConsumerApplication {
    public static void main(String[] args) {
        final ConfigurableApplicationContext context = SpringApplication.run(ConsumerApplication.class, args);
        final ConsumerService consumerService = context.getBean(ConsumerService.class);

        final CompletableFuture<List<Employee>> future = consumerService.hiredEmployees()
                .thenCompose(employees -> {
                    final List<CompletableFuture<Employee>> listCf = employees.stream()
                            .map(employee ->
                                    consumerService.getSalary(employee.getId())
                                            .thenApply(sal -> {
                                                employee.setSalary(sal);
                                                return employee;
                                            }))
                            .collect(Collectors.toList());

                    final CompletableFuture<List<Employee>> sequence = sequence(listCf);
                    return sequence;
                });

        future.thenAccept(employees -> log.info("Employees: {}", employees))
                .thenRun(context::close);
    }

    private static <T> CompletableFuture<List<T>> sequence(List<CompletableFuture<T>> futures) {
        final CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

        return allOf.thenApply(v ->
                futures.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList())
        );
    }

    private CompletableFuture<Double> getSalary(Employee employee, ConsumerService consumerService) {
        return consumerService.getSalary(employee.getId());
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
