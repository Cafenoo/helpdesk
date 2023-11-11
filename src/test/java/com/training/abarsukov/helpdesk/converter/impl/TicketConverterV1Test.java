package com.training.abarsukov.helpdesk.converter.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.training.abarsukov.helpdesk.converter.UserConverter;
import com.training.abarsukov.helpdesk.dto.TicketDto;
import com.training.abarsukov.helpdesk.dto.UserDto;
import com.training.abarsukov.helpdesk.model.Category;
import com.training.abarsukov.helpdesk.model.Ticket;
import com.training.abarsukov.helpdesk.model.User;
import com.training.abarsukov.helpdesk.model.enums.State;
import com.training.abarsukov.helpdesk.model.enums.Urgency;
import java.sql.Date;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TicketConverterV1Test {

  @InjectMocks 
  private TicketConverterV1 ticketConverter;

  @Mock 
  private UserConverter userConverter;

  @Test
  void testConvertToEntity() {
    final String name = "Ticket 1";
    final String description = "Description 1";
    final Date createdOn = Date.valueOf("2020-02-20");
    final Date desiredResolutionDate = Date.valueOf("2020-03-05");
    final State state = State.DECLINED;
    final Category category = Category.builder()
        .id(1L)
        .name("Category 1")
        .build();
    final Urgency urgency = Urgency.AVERAGE;

    final TicketDto ticketDto = TicketDto.builder()
        .name(name)
        .description(description)
        .createdOn(createdOn)
        .desiredResolutionDate(desiredResolutionDate)
        .state(state)
        .category(category)
        .urgency(urgency)
        .build();

    final Ticket actualTicket = ticketConverter.convertToEntity(ticketDto);

    assertEquals(name, actualTicket.getName());
    assertEquals(description, actualTicket.getDescription());
    assertEquals(createdOn, actualTicket.getCreatedOn());
    assertEquals(desiredResolutionDate, actualTicket.getDesiredResolutionDate());
    assertEquals(state, actualTicket.getState());
    assertEquals(category, actualTicket.getCategory());
    assertEquals(urgency, actualTicket.getUrgency());

    verifyNoInteractions(userConverter);
  }

  @Test
  void testConvertToTicketListDto() {
    final Long id = 15L;
    final String name = "Ticket 15";
    final Date desiredResolutionDate = Date.valueOf("2020-09-12");
    final Urgency urgency = Urgency.LOW;
    final State state = State.NEW;

    final Ticket ticket = Ticket.builder()
        .id(id)
        .name(name)
        .desiredResolutionDate(desiredResolutionDate)
        .urgency(urgency)
        .state(state)
        .build();

    final TicketDto actualTicketDto = ticketConverter.convertToTicketListDto(ticket);

    assertEquals(id, actualTicketDto.getId());
    assertEquals(name, actualTicketDto.getName());
    assertEquals(desiredResolutionDate, actualTicketDto.getDesiredResolutionDate());
    assertEquals(urgency, actualTicketDto.getUrgency());
    assertEquals(state, actualTicketDto.getState());

    verifyNoInteractions(userConverter);
  }

  @Test
  void testConvertToDto() {
    final Long id = 1L;
    final String name = "Ticket 1";
    final Date createdOn = Date.valueOf("2020-02-20");
    final State state = State.DECLINED;
    final Category category = Category.builder()
        .id(1L)
        .name("Category 1")
        .build();
    final Urgency urgency = Urgency.AVERAGE;
    final String description = "Description 1";
    final Date desiredResolutionDate = Date.valueOf("2020-03-05");
    final User owner = User.builder()
        .firstName("John")
        .lastName("Wick")
        .build();
    final UserDto ownerDto = UserDto.builder()
        .firstName("John")
        .lastName("Wick")
        .build();

    final Ticket ticket = Ticket.builder()
        .id(id)
        .name(name)
        .createdOn(createdOn)
        .state(state)
        .category(category)
        .urgency(urgency)
        .description(description)
        .desiredResolutionDate(desiredResolutionDate)
        .owner(owner)
        .build();

    when(userConverter.convertToDto(owner))
        .thenReturn(ownerDto);

    final TicketDto actualTicketDto = ticketConverter.convertToDto(ticket);

    assertEquals(id, actualTicketDto.getId());
    assertEquals(name, actualTicketDto.getName());
    assertEquals(createdOn, actualTicketDto.getCreatedOn());
    assertEquals(state, actualTicketDto.getState());
    assertEquals(category, actualTicketDto.getCategory());
    assertEquals(urgency, actualTicketDto.getUrgency());
    assertEquals(description, actualTicketDto.getDescription());
    assertEquals(desiredResolutionDate, actualTicketDto.getDesiredResolutionDate());
    assertEquals(ownerDto, actualTicketDto.getOwner());

    verify(userConverter, times(1))
        .convertToDto(owner);

    verifyNoMoreInteractions(userConverter);
  }

  @Test
  void testConvertToDtoWithAdditionalFields() {
    final Long id = 1L;
    final String name = "Ticket 1";
    final Date createdOn = Date.valueOf("2020-02-20");
    final State state = State.DECLINED;
    final Category category = Category.builder()
        .id(1L)
        .name("Category 1")
        .build();
    final Urgency urgency = Urgency.AVERAGE;
    final String description = "Description 1";
    final Date desiredResolutionDate = Date.valueOf("2020-03-05");
    final User owner = User.builder()
        .firstName("John")
        .lastName("Wick")
        .build();
    final UserDto ownerDto = UserDto.builder()
        .firstName("John")
        .lastName("Wick")
        .build();
    final User approver = User.builder()
        .firstName("Approver")
        .lastName("Nickson")
        .build();
    final UserDto approverDto = UserDto.builder()
        .firstName("Approver")
        .lastName("Nickson")
        .build();
    final User assignee = User.builder()
        .firstName("Assignee")
        .lastName("Lawson")
        .build();
    final UserDto assigneeDto = UserDto.builder()
        .firstName("Assignee")
        .lastName("Lawson")
        .build();

    final Ticket ticket = Ticket.builder()
        .id(id)
        .name(name)
        .createdOn(createdOn)
        .state(state)
        .category(category)
        .urgency(urgency)
        .description(description)
        .desiredResolutionDate(desiredResolutionDate)
        .owner(owner)
        .assignee(assignee)
        .approver(approver)
        .build();

    when(userConverter.convertToDto(owner))
        .thenReturn(ownerDto);
    when(userConverter.convertToDto(assignee))
        .thenReturn(assigneeDto);
    when(userConverter.convertToDto(approver))
        .thenReturn(approverDto);

    final TicketDto actualTicketDto = ticketConverter.convertToDto(ticket);

    assertEquals(id, actualTicketDto.getId());
    assertEquals(name, actualTicketDto.getName());
    assertEquals(createdOn, actualTicketDto.getCreatedOn());
    assertEquals(state, actualTicketDto.getState());
    assertEquals(category, actualTicketDto.getCategory());
    assertEquals(urgency, actualTicketDto.getUrgency());
    assertEquals(description, actualTicketDto.getDescription());
    assertEquals(desiredResolutionDate, actualTicketDto.getDesiredResolutionDate());
    assertEquals(ownerDto, actualTicketDto.getOwner());
    assertEquals(assigneeDto, actualTicketDto.getAssignee());
    assertEquals(approverDto, actualTicketDto.getApprover());

    verify(userConverter, times(1))
        .convertToDto(owner);
    verify(userConverter, times(1))
        .convertToDto(assignee);
    verify(userConverter, times(1))
        .convertToDto(approver);

    verifyNoMoreInteractions(userConverter);
  }
}
