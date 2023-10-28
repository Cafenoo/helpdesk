package com.training.abarsukov.helpdesk.service.impl;

import com.training.abarsukov.helpdesk.model.Ticket;
import com.training.abarsukov.helpdesk.repository.TicketRepository;
import com.training.abarsukov.helpdesk.security.UserService;
import com.training.abarsukov.helpdesk.service.TicketAccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

@Service
@RequiredArgsConstructor
public class TicketAccessHandlerV1 implements TicketAccessHandler {

  private final UserService userService;

  private final TicketRepository ticketRepository;

  @Override
  @Transactional(propagation = SUPPORTS, readOnly = true)
  public Ticket findByIdToView(Long id) {
    return ticketRepository
        .findById(id, userService.getUser())
        .orElseThrow(EntityNotFoundException::new);
  }

  @Override
  @Transactional(propagation = SUPPORTS, readOnly = true)
  public Ticket findByIdToEdit(Long id) {
    return ticketRepository
        .findByIdToEditTicket(id, userService.getUser())
        .orElseThrow(EntityNotFoundException::new);
  }

  @Override
  @Transactional(propagation = SUPPORTS, readOnly = true)
  public Ticket findByIdToSaveFeedback(Long id) {
    return ticketRepository
        .findByIdToSaveFeedback(id, userService.getUser())
        .orElseThrow(EntityNotFoundException::new);
  }
}
