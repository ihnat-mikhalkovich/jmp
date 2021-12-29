package com.epam.learn.nosql.taskmanager.validator;

import javax.validation.Constraint;
import javax.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidObjectIdTaskValidator.class)
public @interface ValidObjectIdTask {
    String message() default "Task.id is not valid.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
