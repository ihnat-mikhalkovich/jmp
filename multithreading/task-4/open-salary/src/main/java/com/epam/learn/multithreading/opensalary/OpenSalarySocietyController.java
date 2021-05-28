package com.epam.learn.multithreading.opensalary;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/employees/")
public class OpenSalarySocietyController {

    private final List<Employee> employees = Stream.iterate(1, i -> ++i).limit(5)
            .map(i -> new Employee(i, "name-" + i, "surname-" + i, "email-" + i))
            .collect(Collectors.toList());

    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Employee>> getAllHiredEmployee() {
        return ResponseEntity.ok(employees);
    }

    @GetMapping(path = "{id}/salary")
    public ResponseEntity<Double> getSalary(@PathVariable long id) {
        return ResponseEntity.ok(id * 11.11);
    }
}
