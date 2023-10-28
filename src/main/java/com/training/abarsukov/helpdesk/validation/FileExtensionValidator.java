package com.training.abarsukov.helpdesk.validation;

import com.training.abarsukov.helpdesk.validation.constraints.FileExtension;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FileExtensionValidator implements ConstraintValidator<FileExtension, MultipartFile> {

  private static final String REGEX = "^.*\\.(pdf|png|doc|docx|jpg|jpeg)$";

  @Override
  public void initialize(FileExtension constraintAnnotation) {}

  @Override
  public boolean isValid(
      MultipartFile file, ConstraintValidatorContext constraintValidatorContext) {
    if (file == null) {
      return false;
    }

    final String fileName = file.getOriginalFilename().toLowerCase();
    return fileName.matches(REGEX);
  }
}
