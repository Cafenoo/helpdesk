package com.training.abarsukov.helpdesk.service;

import com.training.abarsukov.helpdesk.model.Ticket;

public interface TicketAccessHandler {
  Ticket findByIdToView(Long id);

  Ticket findByIdToEdit(Long id);

  Ticket findByIdToSaveFeedback(Long id);
}
