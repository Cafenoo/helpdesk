package com.training.abarsukov.helpdesk.validation;

import com.training.abarsukov.helpdesk.validation.constraints.DateNotLessThanCurrentDate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.sql.Date;
import java.time.LocalDate;

public class DateNotLessThanCurrentDateValidator
    implements ConstraintValidator<DateNotLessThanCurrentDate, Date> {

  @Override
  public void initialize(DateNotLessThanCurrentDate date) {}

  @Override
  public boolean isValid(Date date, ConstraintValidatorContext cxt) {
    if (date == null) {
      return true;
    }

    Date now = Date.valueOf(LocalDate.now());
    return date.equals(now) || date.after(now);
  }
}
