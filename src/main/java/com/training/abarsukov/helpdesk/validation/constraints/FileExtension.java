package com.training.abarsukov.helpdesk.validation.constraints;

import com.training.abarsukov.helpdesk.validation.FileExtensionValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = FileExtensionValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FileExtension {
  String message() default
      "The selected file type is not allowed."
          + " Please select a file of one of the following types: pdf, png, doc, docx, jpg, jpeg.";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
