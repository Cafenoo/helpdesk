package com.training.abarsukov.helpdesk.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.mock.mockito.MockBean;

class EmailValidatorTest {

  @MockBean
  private ConstraintValidatorContext context;

  private EmailValidator emailValidator;

  @BeforeEach
  void setUp() {
    emailValidator = new EmailValidator();
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "example@ex.com", 
      "second_example@example.net"
  })
  void testIsValidWithPositiveCases(String valueToVerify) {
    assertTrue(emailValidator.isValid(valueToVerify, context));
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "example@excom", 
      "exampleex.com", 
      "@example", 
      "example@", 
      ".example", 
      "example.", 
      "example", 
      "e@.m", 
      ""
  })
  void testIsValidWithNegativeCases(String valueToVerify) {
    assertFalse(emailValidator.isValid(valueToVerify, context));
  }

  @Test
  void testIsValidWithNull() {
    assertFalse(emailValidator.isValid(null, context));
  }
}
