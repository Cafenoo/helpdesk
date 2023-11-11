package com.training.abarsukov.helpdesk.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.mock.mockito.MockBean;

class PasswordValidatorTest {

  @MockBean 
  private ConstraintValidatorContext context;

  private PasswordValidator passwordValidator;

  @BeforeEach
  void setUp() {
    passwordValidator = new PasswordValidator();
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "P~ssword1",
      "P.ssword1",
      "P\"ssword1",
      "P(ssword1",
      "P)ssword1",
      "P,ssword1",
      "P:ssword1",
      "P;ssword1",
      "P<ssword1",
      "P>ssword1",
      "P@ssword1",
      "P[ssword1",
      "P]ssword1",
      "P!ssword1",
      "P#ssword1",
      "P$ssword1",
      "P%ssword1",
      "P&ssword1",
      "P'ssword1",
      "P*ssword1",
      "P+ssword1",
      "P-ssword1",
      "P/ssword1",
      "P=ssword1",
      "P?ssword1",
      "P^ssword1",
      "P_ssword1",
      "P`ssword1",
      "P{ssword1",
      "P|ssword1",
      "P}ssword1",
      "P@ssword1",
      "P@ssword1",
      "P@ssword1",
      "P@ssword1",
      "Yh2-josdv"
  })
  void testIsValidWithPositiveCases(String valueToVerify) {
    assertTrue(passwordValidator.isValid(valueToVerify, context));
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "p@ssword1",
      "P@SSWORD1",
      "Password1",
      "P@ssword",
      ""
  })
  void testIsValidWithNegativeCases(String valueToVerify) {
    assertFalse(passwordValidator.isValid(valueToVerify, context));
  }

  @Test
  void testIsValidWithNull() {
    assertFalse(passwordValidator.isValid(null, context));
  }
}
