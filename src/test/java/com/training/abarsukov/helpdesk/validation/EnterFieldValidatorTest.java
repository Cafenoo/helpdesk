package com.training.abarsukov.helpdesk.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.mock.mockito.MockBean;

class EnterFieldValidatorTest {

  @MockBean 
  private ConstraintValidatorContext context;

  private EnterFieldValidator enterFieldValidator;

  @BeforeEach
  void setUp() {
    enterFieldValidator = new EnterFieldValidator();
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
      "Yh2-josdv",
      "pasdfasdf",
      "2124523",
      "lksdg325r nj"
  })
  void testIsValidWithPositiveCases(String valueToVerify) {
    assertTrue(enterFieldValidator.isValid(valueToVerify, context));
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "\\", 
      "P\\ssword", 
      ""
  })
  void testIsValidWithNegativeCases(String valueToVerify) {
    assertFalse(enterFieldValidator.isValid(valueToVerify, context));
  }

  @Test
  void testIsValidWithNull() {
    assertTrue(enterFieldValidator.isValid(null, context));
  }
}
