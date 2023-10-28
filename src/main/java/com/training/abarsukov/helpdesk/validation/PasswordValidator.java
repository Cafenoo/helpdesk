package com.training.abarsukov.helpdesk.validation;

import com.training.abarsukov.helpdesk.validation.constraints.Password;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<Password, String> {

  private static final String REGEX =
      "^(?=.*?[A-Z])"
          + "(?=.*?[a-z])"
          + "(?=.*?[0-9])"
          + "(?=.*?[~.\"(),:;<>@\\[\\]!#$%&'*+\\-/=?^_`{|}])"
          + ".+$";

  @Override
  public void initialize(Password password) {}

  @Override
  public boolean isValid(String contactField, ConstraintValidatorContext cxt) {
    if (contactField == null) {
      return false;
    }
    return contactField.matches(REGEX);
  }
}
