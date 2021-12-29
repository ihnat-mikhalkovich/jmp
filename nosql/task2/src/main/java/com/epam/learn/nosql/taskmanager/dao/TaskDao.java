package com.epam.learn.nosql.taskmanager.dao;

import com.epam.learn.nosql.taskmanager.entity.Subtask;
import com.epam.learn.nosql.taskmanager.entity.Task;

import java.util.List;
import java.util.stream.Collectors;

public interface TaskDao {
    List<Task> findAll();

    List<Task> overdueTasks();

    List<Task> findAll(String category);

    default List<Subtask> findAllSubtasksOfTasksOfCategory(String category) {
        return findAll(category).stream()
                .flatMap(task -> task.getSubtasks().stream())
                .distinct()
                .collect(Collectors.toList());
    }

    String createTask(Task task);

    void deleteTask(String id);

    Task updateTask(Task task);

    List<Subtask> createSubtasks(Task task);

    List<Subtask> deleteSubtasks(Task task);

    List<Task> fullTextSearch(String text);
}
