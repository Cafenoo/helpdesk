package com.training.abarsukov.helpdesk.validation.constraints;

import com.training.abarsukov.helpdesk.validation.DateNotLessThanCurrentDateValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = DateNotLessThanCurrentDateValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DateNotLessThanCurrentDate {
  String message() default "Not able to select a date that is less than the current date";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
