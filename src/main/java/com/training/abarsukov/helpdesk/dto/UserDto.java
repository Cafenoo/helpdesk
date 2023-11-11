package com.training.abarsukov.helpdesk.dto;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.training.abarsukov.helpdesk.validation.constraints.Email;
import com.training.abarsukov.helpdesk.validation.constraints.Password;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@JsonInclude(NON_NULL)
@Jacksonized
public class UserDto {

  @Email(message = "Please make sure you are using a valid email")
  @Size(max = 100, message = "Please make sure email is less than 100 characters")
  private String email;

  @JsonProperty(access = WRITE_ONLY)
  @Password(message = "Please make sure you are using a valid password")
  @Size(
      min = 6,
      max = 20,
      message = "Please make sure password is in range between 6 and 20 characters")
  private String password;

  private String firstName;

  private String lastName;
}
