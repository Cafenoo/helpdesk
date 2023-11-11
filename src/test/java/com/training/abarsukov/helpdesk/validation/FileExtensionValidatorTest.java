package com.training.abarsukov.helpdesk.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

class FileExtensionValidatorTest {

  @MockBean 
  private ConstraintValidatorContext context;

  private FileExtensionValidator fileExtensionValidator;

  @BeforeEach
  void setUp() {
    fileExtensionValidator = new FileExtensionValidator();
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "file.pdf",
      "file.doc",
      "file.docx", 
      "file.png", 
      "file.jpeg", 
      "file.jpg"
  })
  void testIsValidWithPositiveCases(String valueToVerify) {
    final byte[] emptyByteArray = {};
    final MockMultipartFile mockMultipartFile =
        new MockMultipartFile(
            valueToVerify, 
            valueToVerify, 
            MediaType.MULTIPART_FORM_DATA_VALUE,
            emptyByteArray
        );

    assertTrue(fileExtensionValidator.isValid(mockMultipartFile, context));
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "file.obj", 
      "obj.bmp", 
      "file", 
      "file.", 
      ".file", 
      "."
  })
  void testIsValidWithNegativeCases(String valueToVerify) {
    final byte[] emptyByteArray = {};
    final MockMultipartFile mockMultipartFile =
        new MockMultipartFile(
            valueToVerify, 
            valueToVerify, 
            MediaType.MULTIPART_FORM_DATA_VALUE, 
            emptyByteArray
        );

    assertFalse(fileExtensionValidator.isValid(mockMultipartFile, context));
  }

  @Test
  void testIsValidWithNull() {
    assertFalse(fileExtensionValidator.isValid(null, context));
  }
}
