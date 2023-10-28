package com.training.abarsukov.helpdesk.validation.constraints;

import com.training.abarsukov.helpdesk.validation.EnterFieldValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = EnterFieldValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface EnterField {
  String message() default
      "Field should have upper- and lowercase letters,"
          + " numbers and special characters ~.\"(),:;<>@[]!#$%&'*+-/=?^_`{|}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
