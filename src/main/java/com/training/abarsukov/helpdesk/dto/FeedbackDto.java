package com.training.abarsukov.helpdesk.dto;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.training.abarsukov.helpdesk.validation.constraints.EnterField;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(NON_NULL)
public class FeedbackDto {

  @NotNull(message = "Rate should not be null")
  @Min(value = 1, message = "Rate should be in range from 1 to 5")
  @Max(value = 5, message = "Rate should be in range from 1 to 5")
  private Integer rate;

  @EnterField
  private String comment;
}
