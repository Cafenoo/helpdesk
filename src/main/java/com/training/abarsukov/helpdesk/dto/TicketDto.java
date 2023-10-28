package com.training.abarsukov.helpdesk.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.training.abarsukov.helpdesk.model.Category;
import com.training.abarsukov.helpdesk.model.enums.Action;
import com.training.abarsukov.helpdesk.model.enums.State;
import com.training.abarsukov.helpdesk.model.enums.Urgency;
import com.training.abarsukov.helpdesk.validation.constraints.DateNotLessThanCurrentDate;
import com.training.abarsukov.helpdesk.validation.constraints.EnterField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Date;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@Builder
@JsonInclude(NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class TicketDto {

  private Long id;

  @NotBlank(message = "Name should not be null")
  @Size(max = 100, message = "Name of the ticket can't be more than 100 characters")
  @EnterField
  private String name;

  @Size(max = 500, message = "Description of the ticket can't be more than 500 characters")
  @EnterField
  private String description;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
  private Date createdOn;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
  @DateNotLessThanCurrentDate
  private Date desiredResolutionDate;

  private State state;

  @NotNull(message = "Category should not be null")
  private Category category;

  @NotNull(message = "Urgency should not be null")
  private Urgency urgency;

  private UserDto assignee;

  private UserDto owner;

  private UserDto approver;

  @Valid private CommentDto comment;

  @Valid private AttachmentDto attachment;

  private List<Action> actions;
}
