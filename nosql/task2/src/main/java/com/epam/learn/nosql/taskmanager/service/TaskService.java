package com.epam.learn.nosql.taskmanager.service;

import com.epam.learn.nosql.taskmanager.entity.Subtask;
import com.epam.learn.nosql.taskmanager.entity.Task;
import com.epam.learn.nosql.taskmanager.validator.ValidObjectIdTask;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
public interface TaskService {

    @NotNull
    List<Task> findAll();

    @NotNull
    List<Task> overdueTasks();

    @NotNull
    List<Task> findAll(@NotNull @NotBlank String category);

    @NotNull
    List<Subtask> findAllSubtasksOfTasksOfCategory(@NotNull @NotBlank String category);

    @NotNull
    @NotBlank
    String createTask(@NotNull Task task);

    void deleteTask(@NotNull @ValidObjectIdTask Task task);

    @NotNull
    Task updateTask(@NotNull @ValidObjectIdTask Task task);

    @NotNull
    List<Subtask> createSubtasks(@NotNull @ValidObjectIdTask Task task);

    @NotNull
    List<Subtask> deleteSubtasks(@NotNull @ValidObjectIdTask Task task);

    @NotNull
    List<Task> fullTextSearch(@NotNull @NotBlank String text);
}
