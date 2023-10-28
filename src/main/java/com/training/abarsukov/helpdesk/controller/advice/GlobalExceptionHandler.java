package com.training.abarsukov.helpdesk.controller.advice;

import com.training.abarsukov.helpdesk.exception.FileException;
import com.training.abarsukov.helpdesk.exception.TicketStateException;
import com.training.abarsukov.helpdesk.exception.description.ErrorMessage;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final String SPACE = " ";

  @ExceptionHandler(MaxUploadSizeExceededException.class)
  public ResponseEntity<ErrorMessage> handleMaxUploadSizeExceededException(
      MaxUploadSizeExceededException exception) {
    final String reason =
        "The size of the attached file should not be greater than 5 Mb. Please select another file.";
    return buildResponse(exception, reason, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ErrorMessage> handleEntityNotFoundException(
      EntityNotFoundException exception) {
    return buildResponse(exception, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(FileException.class)
  public ResponseEntity<ErrorMessage> handleFileException(FileException exception) {
    return buildResponse(exception, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorMessage> handleConstraintViolationException(
      ConstraintViolationException exception) {
    return buildResponse(exception, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(TicketStateException.class)
  public ResponseEntity<ErrorMessage> handleTicketStateTransitionException(
      TicketStateException exception) {
    return buildResponse(exception, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ErrorMessage> handleDataIntegrityViolationException(
      DataIntegrityViolationException exception) {
    return buildResponse(exception, HttpStatus.LOCKED);
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<ErrorMessage> handleMissingServletRequestParameterException(
      MissingServletRequestParameterException exception) {
    return buildResponse(exception, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorMessage> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException exception) {
    final List<String> reasons =
        exception.getBindingResult().getAllErrors().stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .collect(Collectors.toList());

    return buildResponse(exception, reasons, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(BindException.class)
  public ResponseEntity<ErrorMessage> handleBeanPropertyBindingResult(BindException exception) {
    final List<String> reasons =
        exception.getBindingResult().getAllErrors().stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .collect(Collectors.toList());

    return buildResponse(exception, reasons, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorMessage> handleIllegalArgumentException(
      IllegalArgumentException exception) {
    return buildResponse(exception, HttpStatus.BAD_REQUEST);
  }

  private ResponseEntity<ErrorMessage> buildResponse(Throwable throwable, HttpStatus status) {
    return buildResponse(throwable, throwable.getMessage(), status);
  }

  private ResponseEntity<ErrorMessage> buildResponse(
      Throwable throwable, List<String> reasons, HttpStatus status) {
    final String concatenatedReasons = String.join(SPACE, reasons);
    return buildResponse(throwable, concatenatedReasons, status);
  }

  private ResponseEntity<ErrorMessage> buildResponse(
      Throwable throwable, String reason, HttpStatus status) {
    return ResponseEntity.status(status).body(new ErrorMessage(throwable, reason, status));
  }
}
