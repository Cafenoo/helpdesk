package com.training.abarsukov.helpdesk.validation;

import com.training.abarsukov.helpdesk.validation.constraints.Email;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailValidator implements ConstraintValidator<Email, String> {

  private static final String REGEX = "[^@ \\t\\r\\n]+@[^@ \\t\\r\\n]+\\.[^@ \\t\\r\\n]+";

  @Override
  public void initialize(Email constraintAnnotation) {}

  @Override
  public boolean isValid(
      String contactField, ConstraintValidatorContext constraintValidatorContext) {
    if (contactField == null) {
      return false;
    }
    return contactField.matches(REGEX);
  }
}
