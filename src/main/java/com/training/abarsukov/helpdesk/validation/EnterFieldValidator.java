package com.training.abarsukov.helpdesk.validation;

import com.training.abarsukov.helpdesk.validation.constraints.EnterField;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EnterFieldValidator implements ConstraintValidator<EnterField, String> {

  private static final String REGEX = "[A-Za-z0-9~.\"(),:;<>@\\[\\]!#$%&'*+-/=?^_`{|} ]*";

  @Override
  public void initialize(EnterField constraintAnnotation) {}

  @Override
  public boolean isValid(
      String contactField, ConstraintValidatorContext constraintValidatorContext) {
    if (contactField == null) {
      return true;
    }
    if (contactField.equals("")) {
      return false;
    }
    return contactField.matches(REGEX);
  }
}
