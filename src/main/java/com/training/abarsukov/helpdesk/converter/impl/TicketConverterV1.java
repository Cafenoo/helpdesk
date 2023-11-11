package com.training.abarsukov.helpdesk.converter.impl;

import static java.util.Objects.nonNull;

import com.training.abarsukov.helpdesk.converter.TicketConverter;
import com.training.abarsukov.helpdesk.converter.UserConverter;
import com.training.abarsukov.helpdesk.dto.TicketDto;
import com.training.abarsukov.helpdesk.model.Ticket;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TicketConverterV1 implements TicketConverter {

  private final UserConverter userConverter;

  @Override
  public Ticket convertToEntity(TicketDto ticketDto) {
    return Ticket.builder()
        .name(ticketDto.getName())
        .description(ticketDto.getDescription())
        .createdOn(ticketDto.getCreatedOn())
        .desiredResolutionDate(ticketDto.getDesiredResolutionDate())
        .state(ticketDto.getState())
        .category(ticketDto.getCategory())
        .urgency(ticketDto.getUrgency())
        .build();
  }

  @Override
  public TicketDto convertToTicketListDto(Ticket ticket) {
    return TicketDto.builder()
        .id(ticket.getId())
        .name(ticket.getName())
        .desiredResolutionDate(ticket.getDesiredResolutionDate())
        .urgency(ticket.getUrgency())
        .state(ticket.getState())
        .build();
  }

  @Override
  public TicketDto convertToDto(Ticket ticket) {
    final TicketDto ticketDto =
        TicketDto.builder()
            .id(ticket.getId())
            .name(ticket.getName())
            .createdOn(ticket.getCreatedOn())
            .state(ticket.getState())
            .category(ticket.getCategory())
            .urgency(ticket.getUrgency())
            .description(ticket.getDescription())
            .desiredResolutionDate(ticket.getDesiredResolutionDate())
            .owner(userConverter.convertToDto(ticket.getOwner()))
            .build();

    if (nonNull(ticket.getAssignee())) {
      ticketDto.setAssignee(userConverter.convertToDto(ticket.getAssignee()));
    }

    if (nonNull(ticket.getApprover())) {
      ticketDto.setApprover(userConverter.convertToDto(ticket.getApprover()));
    }

    return ticketDto;
  }
}
