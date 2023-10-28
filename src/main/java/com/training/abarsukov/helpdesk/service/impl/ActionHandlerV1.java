package com.training.abarsukov.helpdesk.service.impl;

import com.training.abarsukov.helpdesk.exception.TicketStateException;
import com.training.abarsukov.helpdesk.model.Ticket;
import com.training.abarsukov.helpdesk.model.enums.Action;
import com.training.abarsukov.helpdesk.model.enums.Role;
import com.training.abarsukov.helpdesk.model.enums.State;
import com.training.abarsukov.helpdesk.security.UserService;
import com.training.abarsukov.helpdesk.service.ActionHandler;
import com.training.abarsukov.helpdesk.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.training.abarsukov.helpdesk.model.enums.Action.APPROVE;
import static com.training.abarsukov.helpdesk.model.enums.Action.ASSIGN_TO_ME;
import static com.training.abarsukov.helpdesk.model.enums.Action.CANCEL;
import static com.training.abarsukov.helpdesk.model.enums.Action.DECLINE;
import static com.training.abarsukov.helpdesk.model.enums.Action.DONE;
import static com.training.abarsukov.helpdesk.model.enums.Action.SUBMIT;
import static com.training.abarsukov.helpdesk.model.enums.Role.EMPLOYEE;
import static com.training.abarsukov.helpdesk.model.enums.Role.ENGINEER;
import static com.training.abarsukov.helpdesk.model.enums.Role.MANAGER;

@Service
@RequiredArgsConstructor
public class ActionHandlerV1 implements ActionHandler {

  private static final Map<Role, List<Action>> DRAFT_COLUMN =
      Map.of(
          EMPLOYEE, List.of(SUBMIT, CANCEL),
          MANAGER, List.of(SUBMIT, CANCEL));

  private static final Map<Role, List<Action>> NEW_COLUMN =
      Map.of(MANAGER, List.of(APPROVE, DECLINE, CANCEL));

  private static final Map<Role, List<Action>> APPROVED_COLUMN =
      Map.of(ENGINEER, List.of(ASSIGN_TO_ME, CANCEL));

  private static final Map<Role, List<Action>> DECLINED_COLUMN = DRAFT_COLUMN;

  private static final Map<Role, List<Action>> CANCELLED_COLUMN = Collections.emptyMap();

  private static final Map<Role, List<Action>> IN_PROGRESS_COLUMN = Map.of(ENGINEER, List.of(DONE));

  private static final Map<Role, List<Action>> DONE_COLUMN = Collections.emptyMap();

  private static final Map<State, Map<Role, List<Action>>> ACTION_TABLE =
      Map.of(
          State.DRAFT, DRAFT_COLUMN,
          State.NEW, NEW_COLUMN,
          State.APPROVED, APPROVED_COLUMN,
          State.DECLINED, DECLINED_COLUMN,
          State.CANCELED, CANCELLED_COLUMN,
          State.IN_PROGRESS, IN_PROGRESS_COLUMN,
          State.DONE, DONE_COLUMN);

  private final EmailService emailService;

  private final UserService userService;

  @Override
  public List<Action> getPossibleActions(State state) {
    final Map<Role, List<Action>> stateColumn = ACTION_TABLE.get(state);
    final Role userRole = userService.getUser().getRole();
    return stateColumn.getOrDefault(userRole, Collections.emptyList());
  }

  @Override
  public void transitState(Ticket ticket, Action action) {
    final List<Action> possibleActions = getPossibleActions(ticket.getState());

    if (!possibleActions.contains(action)) {
      throw new TicketStateException("Unable to transit ticket status");
    }

    final Map<Action, Runnable> transitionInstruction =
        Map.of(
            SUBMIT, () -> handleSubmit(ticket),
            APPROVE, () -> handleApprove(ticket),
            DECLINE, () -> handleDecline(ticket),
            CANCEL, () -> handleCancel(ticket),
            ASSIGN_TO_ME, () -> handleAssignToMe(ticket),
            DONE, () -> handleDone(ticket));

    transitionInstruction.get(action).run();
  }

  private void handleSubmit(Ticket ticket) {
    ticket.setState(State.NEW);
    emailService.sendMailThatTicketHasToBeApproved(ticket);
  }

  private void handleApprove(Ticket ticket) {
    ticket.setState(State.APPROVED);
    ticket.setApprover(userService.getUser());
    emailService.sendMailThatTicketHasBeenApproved(ticket);
  }

  private void handleDecline(Ticket ticket) {
    ticket.setState(State.DECLINED);
    emailService.sendMailThatTicketHasBeenDeclined(ticket);
  }

  private void handleCancel(Ticket ticket) {
    ticket.setState(State.CANCELED);
    final Role userRole = userService.getUser().getRole();
    final Map<Role, Runnable> instructionToSendEmailInCanceled =
        Map.of(
            MANAGER, () -> emailService.sendMailThatTicketHasBeenCancelled(ticket),
            ENGINEER, () -> emailService.sendMailThatTicketHasBeenCancelledByEngineer(ticket));
    instructionToSendEmailInCanceled.get(userRole).run();
  }

  private void handleAssignToMe(Ticket ticket) {
    ticket.setState(State.IN_PROGRESS);
    ticket.setAssignee(userService.getUser());
  }

  private void handleDone(Ticket ticket) {
    ticket.setState(State.DONE);
    emailService.sendMailThatTicketHasBeenDone(ticket);
  }
}
