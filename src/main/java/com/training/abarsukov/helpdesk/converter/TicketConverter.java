package com.training.abarsukov.helpdesk.converter;

import com.training.abarsukov.helpdesk.dto.TicketDto;
import com.training.abarsukov.helpdesk.model.Ticket;

public interface TicketConverter {
  Ticket convertToEntity(TicketDto ticketDto);

  TicketDto convertToTicketListDto(Ticket ticket);

  TicketDto convertToDto(Ticket ticket);
}
