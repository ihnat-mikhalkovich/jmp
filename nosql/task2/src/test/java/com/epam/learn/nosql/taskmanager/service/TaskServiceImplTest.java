package com.epam.learn.nosql.taskmanager.service;

import com.epam.learn.nosql.taskmanager.dao.TaskDao;
import com.epam.learn.nosql.taskmanager.entity.Task;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.executable.ExecutableValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Method;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class TaskServiceImplTest {

    TaskService taskService;

    @BeforeEach
    void init(@Mock TaskDao taskDao) {
        taskService = new TaskServiceImpl(taskDao);
    }

    @Test
    public void nullTaskIdTest() throws NoSuchMethodException {
        final Task task = new Task();
        task.setId(null);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        final ExecutableValidator executableValidator = factory.getValidator().forExecutables();
        final Method deleteTaskMethod = taskService.getClass().getMethod("deleteTask", Task.class);
        final Object[] parameterValues = {task};
        final Set<ConstraintViolation<TaskService>> constraintViolations = executableValidator.validateParameters(
                taskService,
                deleteTaskMethod,
                parameterValues
        );

        taskService.deleteTask(task);

        System.out.println(constraintViolations);
    }

    @Test
    public void passNullStringTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        taskService.findAll(null);
    }
}