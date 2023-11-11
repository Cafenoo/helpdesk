package com.training.abarsukov.helpdesk.dto;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.training.abarsukov.helpdesk.validation.constraints.EnterField;
import java.sql.Timestamp;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
