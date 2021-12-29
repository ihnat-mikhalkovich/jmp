package com.epam.learn.nosql.taskmanager.validator;

import com.epam.learn.nosql.taskmanager.entity.Task;
import org.bson.types.ObjectId;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidator;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidatorContext;

@Component
public class ValidObjectIdTaskValidator implements HibernateConstraintValidator<ValidObjectIdTask, Task> {

    @Override
    public void initialize(ValidObjectIdTask constraintAnnotation) {
        HibernateConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Task task, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        if (task.getId() == null) {
            context.buildConstraintViolationWithTemplate("Task.id should not be null.");
            return false;
        }

        if (task.getId().isEmpty()) {
            context.buildConstraintViolationWithTemplate("Task.id should not be empty.");
            return false;
        }

        if (!ObjectId.isValid(task.getId())) {
            context.buildConstraintViolationWithTemplate("Task.id should be valid ObjectId.");
            return false;
        }

        return true;
    }
}
