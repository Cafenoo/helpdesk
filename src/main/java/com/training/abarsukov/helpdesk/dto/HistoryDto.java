package com.training.abarsukov.helpdesk.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class HistoryDto {

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd HH:mm")
  private Timestamp timestamp;

  private UserDto user;

  private String action;

  private String description;
}
