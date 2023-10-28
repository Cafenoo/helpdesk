package com.training.abarsukov.helpdesk.validation.constraints;

import com.training.abarsukov.helpdesk.validation.PasswordValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Password {
  String message() default "Password is invalid";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
