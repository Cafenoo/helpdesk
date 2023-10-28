package com.training.abarsukov.helpdesk.exception.description;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@JsonInclude(NON_NULL)
public class ErrorMessage {

  private final Integer responseCode;

  private final String responseMessage;

  private final String exceptionClass;

  private final String exceptionMessage;

  public ErrorMessage(Throwable throwable, String reason, HttpStatus status) {
    this.responseCode = status.value();
    this.responseMessage = status.getReasonPhrase();
    this.exceptionClass = throwable.getClass().getSimpleName();
    this.exceptionMessage = reason;
  }
}
