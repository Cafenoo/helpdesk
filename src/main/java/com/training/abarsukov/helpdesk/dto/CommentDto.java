package com.training.abarsukov.helpdesk.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.training.abarsukov.helpdesk.validation.constraints.EnterField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.sql.Timestamp;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

  @NotBlank(message = "Text should not be null")
  @EnterField
  @Size(max = 500, message = "Text should have less than 500 characters")
  private String text;

  @JsonFormat(shape = STRING, pattern = "yyyy/MM/dd HH:mm")
  private Timestamp timestamp;

  private UserDto user;
}
