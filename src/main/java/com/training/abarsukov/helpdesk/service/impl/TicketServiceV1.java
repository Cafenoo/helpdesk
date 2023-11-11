package com.training.abarsukov.helpdesk.service.impl;

import static java.text.MessageFormat.format;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

import com.training.abarsukov.helpdesk.converter.TicketConverter;
import com.training.abarsukov.helpdesk.dto.TicketDto;
import com.training.abarsukov.helpdesk.exception.TicketStateException;
import com.training.abarsukov.helpdesk.model.Ticket;
import com.training.abarsukov.helpdesk.model.enums.Action;
import com.training.abarsukov.helpdesk.model.enums.SortingField;
import com.training.abarsukov.helpdesk.model.enums.State;
import com.training.abarsukov.helpdesk.repository.TicketRepository;
import com.training.abarsukov.helpdesk.security.UserService;
import com.training.abarsukov.helpdesk.service.ActionHandler;
import com.training.abarsukov.helpdesk.service.AttachmentService;
import com.training.abarsukov.helpdesk.service.CommentService;
import com.training.abarsukov.helpdesk.service.EmailService;
import com.training.abarsukov.helpdesk.service.HistoryService;
import com.training.abarsukov.helpdesk.service.TicketService;
import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TicketServiceV1 implements TicketService {

  private final UserService userService;

  private final TicketRepository ticketRepository;

  private final TicketConverter ticketConverter;

  private final CommentService commentService;

  private final HistoryService historyService;

  private final AttachmentService attachmentService;

  private final ActionHandler actionHandler;

  private final EmailService emailService;

  @Override
  @Transactional(propagation = SUPPORTS, readOnly = true)
  public List<TicketDto> findAll(
      Integer page,
      Integer pageSize,
      SortingField sortingField,
      String keyword,
      Boolean isPersonal) {
    final List<Ticket> tickets = ticketRepository.findAll(
        page,
        pageSize,
        isPersonal,
        keyword,
        userService.getUser());

    final List<TicketDto> ticketDtoList = tickets.stream()
        .sorted(sortingField.getComparator())
        .map(ticketConverter::convertToTicketListDto)
        .collect(Collectors.toList());

    ticketDtoList.forEach(
        ticketDto -> ticketDto.setActions(
            actionHandler.getPossibleActions(ticketDto.getState())));

    return ticketDtoList;
  }

  @Override
  @Transactional(propagation = SUPPORTS, readOnly = true)
  public TicketDto findById(Long id) {
    final Ticket ticket = ticketRepository
        .findById(id, userService.getUser())
        .orElseThrow(EntityNotFoundException::new);

    final TicketDto ticketDto = ticketConverter.convertToDto(ticket);
    ticketDto.setActions(actionHandler.getPossibleActions(ticket.getState()));
    return ticketDto;
  }

  @Override
  @Transactional
  public Ticket save(TicketDto ticketDto) {
    final State state = ofNullable(ticketDto.getState()).orElse(State.NEW);
    if (state != State.DRAFT && state != State.NEW) {
      throw new TicketStateException("Ticket state after creation can only be DRAFT or NEW");
    }

    final Ticket ticket = ticketConverter.convertToEntity(ticketDto);
    ticket.setState(state);
    ticket.setOwner(userService.getUser());
    ticket.setCreatedOn(new Date(System.currentTimeMillis()));

    final Ticket savedTicket = ticketRepository.save(ticket);

    final String action = "Ticket is created";
    historyService.save(savedTicket.getId(), action, action);

    if (nonNull(ticketDto.getComment())) {
      commentService.save(savedTicket.getId(), ticketDto.getComment());
    }

    if (nonNull(ticketDto.getAttachment())) {
      attachmentService.save(savedTicket.getId(), ticketDto.getAttachment());
    }

    if (savedTicket.getState() == State.NEW) {
      emailService.sendMailThatTicketHasToBeApproved(savedTicket);
    }

    return savedTicket;
  }

  @Override
  @Transactional
  public void edit(Long id, TicketDto ticketDto) {
    final Ticket ticket = ticketRepository
        .findByIdToEditTicket(id, userService.getUser())
        .orElseThrow(EntityNotFoundException::new);

    final Ticket convertedTicket = ticketConverter.convertToEntity(ticketDto);
    convertedTicket.setId(id);
    convertedTicket.setCreatedOn(ticket.getCreatedOn());
    convertedTicket.setApprover(ticket.getApprover());
    convertedTicket.setAssignee(ticket.getAssignee());
    convertedTicket.setOwner(ticket.getOwner());

    final State state = ofNullable(ticketDto.getState()).orElse(State.NEW);
    if (state != State.DRAFT && state != State.NEW) {
      throw new TicketStateException("Unable to change ticket's state to " + state);
    }
    convertedTicket.setState(state);

    if (nonNull(ticketDto.getAttachment())) {
      attachmentService.edit(id, ticketDto.getAttachment());
    }

    ticketRepository.update(convertedTicket);

    if (convertedTicket.getState() == State.NEW) {
      emailService.sendMailThatTicketHasToBeApproved(convertedTicket);
    }

    final String action = "Ticket is edited";
    historyService.save(id, action, action);
  }

  @Override
  @Transactional
  public void transitState(Long id, Action action) {
    final Ticket ticket = ticketRepository
        .findById(id, userService.getUser())
        .orElseThrow(EntityNotFoundException::new);

    final State previousState = ticket.getState();

    actionHandler.transitState(ticket, action);
    ticketRepository.update(ticket);

    final String historyAction = "Ticket Status is changed";
    final String description = format(
        "{0} from {1} to {2}",
        historyAction, previousState, ticket.getState());
    historyService.save(id, historyAction, description);
  }
}
