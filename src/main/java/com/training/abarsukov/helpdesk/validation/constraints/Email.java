package com.training.abarsukov.helpdesk.validation.constraints;

import com.training.abarsukov.helpdesk.validation.EmailValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = EmailValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Email {
  String message() default "Email is invalid";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
