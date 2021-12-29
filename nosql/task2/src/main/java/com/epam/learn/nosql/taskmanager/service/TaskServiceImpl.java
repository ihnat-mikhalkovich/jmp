package com.epam.learn.nosql.taskmanager.service;

import com.epam.learn.nosql.taskmanager.dao.TaskDao;
import com.epam.learn.nosql.taskmanager.entity.Subtask;
import com.epam.learn.nosql.taskmanager.entity.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskDao taskDao;

    @Override
    public List<Task> findAll() {
        return taskDao.findAll();
    }

    @Override
    public List<Task> overdueTasks() {
        return taskDao.overdueTasks();
    }

    @Override
    public List<Task> findAll(String category) {
        return taskDao.findAll(category);
    }

    @Override
    public List<Subtask> findAllSubtasksOfTasksOfCategory(String category) {
        return taskDao.findAllSubtasksOfTasksOfCategory(category);
    }

    @Override
    public String createTask(Task task) {
        return taskDao.createTask(task);
    }

    @Override
    public void deleteTask(Task task) {
        taskDao.deleteTask(task.getId());
    }

    @Override
    public Task updateTask(Task task) {
        return taskDao.updateTask(task);
    }

    @Override
    public List<Subtask> createSubtasks(Task task) {
        return taskDao.createSubtasks(task);
    }

    @Override
    public List<Subtask> deleteSubtasks(Task task) {
        return taskDao.deleteSubtasks(task);
    }

    @Override
    public List<Task> fullTextSearch(String text) {
        return taskDao.fullTextSearch(text);
    }
}
