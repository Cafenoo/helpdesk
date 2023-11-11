package com.training.abarsukov.helpdesk.controller;

import static com.training.abarsukov.helpdesk.model.enums.Action.ASSIGN_TO_ME;
import static com.training.abarsukov.helpdesk.model.enums.Action.DECLINE;
import static com.training.abarsukov.helpdesk.model.enums.Action.DONE;
import static com.training.abarsukov.helpdesk.model.enums.State.APPROVED;
import static com.training.abarsukov.helpdesk.model.enums.State.CANCELED;
import static com.training.abarsukov.helpdesk.model.enums.State.DECLINED;
import static com.training.abarsukov.helpdesk.model.enums.State.IN_PROGRESS;
import static com.training.abarsukov.helpdesk.model.enums.Urgency.AVERAGE;
import static com.training.abarsukov.helpdesk.model.enums.Urgency.CRITICAL;
import static com.training.abarsukov.helpdesk.model.enums.Urgency.HIGH;
import static com.training.abarsukov.helpdesk.model.enums.Urgency.LOW;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.training.abarsukov.helpdesk.dto.TicketDto;
import com.training.abarsukov.helpdesk.dto.UserDto;
import com.training.abarsukov.helpdesk.model.Category;
import com.training.abarsukov.helpdesk.model.Ticket;
import com.training.abarsukov.helpdesk.model.enums.Action;
import com.training.abarsukov.helpdesk.model.enums.SortingField;
import com.training.abarsukov.helpdesk.service.TicketService;
import java.sql.Date;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class TicketControllerTest {

  private final String CONTROLLER_URL = "/api/v1/tickets";

  @Autowired 
  private MockMvc mockMvc;

  @MockBean 
  private TicketService ticketService;

  @Test
  void testGetAll() throws Exception {
    final TicketDto ticketDto1 = TicketDto.builder()
        .id(1L)
        .name("Ticket 1")
        .desiredResolutionDate(Date.valueOf("2020-02-05"))
        .urgency(AVERAGE)
        .state(IN_PROGRESS)
        .actions(List.of(DONE))
        .build();

    final TicketDto ticketDto2 = TicketDto.builder()
        .id(2L)
        .name("Ticket 2")
        .desiredResolutionDate(Date.valueOf("2020-02-07"))
        .urgency(LOW)
        .state(APPROVED)
        .actions(List.of(ASSIGN_TO_ME, DECLINE))
        .build();

    final List<TicketDto> tickets = List.of(ticketDto1, ticketDto2);

    final int page = 1;
    final int pageSize = 10;
    final SortingField field = SortingField.DEFAULT;
    final boolean isPersonal = false;

    when(ticketService.findAll(eq(page), eq(pageSize), eq(field), any(), eq(isPersonal)))
        .thenReturn(tickets);

    mockMvc
        .perform(get(CONTROLLER_URL).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id", is(1)))
        .andExpect(jsonPath("$[0].name", is("Ticket 1")))
        .andExpect(jsonPath("$[0].desiredResolutionDate", is("05/02/2020")))
        .andExpect(jsonPath("$[0].urgency", is(AVERAGE.name())))
        .andExpect(jsonPath("$[0].state", is(IN_PROGRESS.name())))
        .andExpect(jsonPath("$[0].actions", is(List.of(DONE.name()))))
        .andExpect(jsonPath("$[1].id", is(2)))
        .andExpect(jsonPath("$[1].name", is("Ticket 2")))
        .andExpect(jsonPath("$[1].desiredResolutionDate", is("07/02/2020")))
        .andExpect(jsonPath("$[1].urgency", is(LOW.name())))
        .andExpect(jsonPath("$[1].state", is(APPROVED.name())))
        .andExpect(jsonPath("$[1].actions", is(List.of(ASSIGN_TO_ME.name(), DECLINE.name()))));

    verify(ticketService, times(1))
        .findAll(eq(page), eq(pageSize), eq(field), any(), eq(isPersonal));
    verifyNoMoreInteractions(ticketService);
  }

  @Test
  void testGetAllWithParams() throws Exception {
    final TicketDto ticketDto1 = TicketDto.builder()
        .id(1L)
        .name("Ticket 1")
        .desiredResolutionDate(Date.valueOf("2020-02-05"))
        .urgency(AVERAGE)
        .state(IN_PROGRESS)
        .actions(List.of(DONE))
        .build();

    final TicketDto ticketDto2 = TicketDto.builder()
        .id(2L)
        .name("Ticket 2")
        .desiredResolutionDate(Date.valueOf("2020-02-07"))
        .urgency(LOW)
        .state(APPROVED)
        .actions(List.of(ASSIGN_TO_ME, DECLINE))
        .build();

    final List<TicketDto> tickets = List.of(ticketDto1, ticketDto2);

    final Integer page = 1;
    final Integer pageSize = 10;
    final SortingField field = SortingField.NAME;
    final String keyword = "keyword";
    final Boolean isPersonal = true;

    when(ticketService.findAll(eq(page), eq(pageSize), eq(field), anyString(), eq(isPersonal)))
        .thenReturn(tickets);

    mockMvc
        .perform(
            get(CONTROLLER_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .param("page", String.valueOf(page))
                .param("pageSize", String.valueOf(pageSize))
                .param("field", field.name())
                .param("keyword", keyword)
                .param("isPersonal", String.valueOf(isPersonal)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id", is(1)))
        .andExpect(jsonPath("$[0].name", is("Ticket 1")))
        .andExpect(jsonPath("$[0].desiredResolutionDate", is("05/02/2020")))
        .andExpect(jsonPath("$[0].urgency", is(AVERAGE.name())))
        .andExpect(jsonPath("$[0].state", is(IN_PROGRESS.name())))
        .andExpect(jsonPath("$[0].actions", is(List.of(DONE.name()))))
        .andExpect(jsonPath("$[1].id", is(2)))
        .andExpect(jsonPath("$[1].name", is("Ticket 2")))
        .andExpect(jsonPath("$[1].desiredResolutionDate", is("07/02/2020")))
        .andExpect(jsonPath("$[1].urgency", is(LOW.name())))
        .andExpect(jsonPath("$[1].state", is(APPROVED.name())))
        .andExpect(jsonPath("$[1].actions", is(List.of(ASSIGN_TO_ME.name(), DECLINE.name()))));

    verify(ticketService, times(1))
        .findAll(eq(page), eq(pageSize), eq(field), anyString(), eq(isPersonal));

    verifyNoMoreInteractions(
        ticketService
    );
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "0",
      "-1",
      "-25",
      "-100"
  })
  void testGetAllWithBadPageParam(String value) throws Exception {
    mockMvc
        .perform(
            get(CONTROLLER_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .param("page", value))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.responseCode", is(400)))
        .andExpect(jsonPath("$.responseMessage", is("Bad Request")))
        .andExpect(jsonPath("$.exceptionClass", is("ConstraintViolationException")));
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "0",
      "-1",
      "-25",
      "-100"
  })
  void testGetAllWithBadPageSizeParam(String value) throws Exception {
    mockMvc
        .perform(
            get(CONTROLLER_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .param("pageSize", value))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.responseCode", is(400)))
        .andExpect(jsonPath("$.responseMessage", is("Bad Request")))
        .andExpect(jsonPath("$.exceptionClass", is("ConstraintViolationException")));
  }

  @Test
  void testGetAllWithBadFieldParam() throws Exception {
    mockMvc
        .perform(
            get(CONTROLLER_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .param("field", "FIELD"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.responseCode", is(400)))
        .andExpect(jsonPath("$.responseMessage", is("Bad Request")))
        .andExpect(jsonPath("$.exceptionClass", is("IllegalArgumentException")));
  }

  @Test
  void testGetAllWithBadIsPersonalParam() throws Exception {
    mockMvc
        .perform(
            get(CONTROLLER_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .param("isPersonal", "troe"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.responseCode", is(400)))
        .andExpect(jsonPath("$.responseMessage", is("Bad Request")))
        .andExpect(jsonPath("$.exceptionClass", is("IllegalArgumentException")));
  }

  @Test
  void testGetById() throws Exception {
    final Long ticketId = 3L;

    final TicketDto ticketDto = TicketDto.builder()
        .id(ticketId)
        .name("Mega important ticket")
        .createdOn(Date.valueOf("2020-05-21"))
        .state(APPROVED)
        .category(Category.builder().id(1L).name("Utilization").build())
        .urgency(CRITICAL)
        .description("Super mega important description")
        .desiredResolutionDate(Date.valueOf("2020-06-06"))
        .owner(UserDto.builder().firstName("Brandon").lastName("Spirit").build())
        .actions(List.of(ASSIGN_TO_ME, DECLINE))
        .assignee(UserDto.builder().firstName("Alex").lastName("Old").build())
        .approver(UserDto.builder().firstName("Oleg").lastName("Permgen").build())
        .build();

    when(ticketService.findById(ticketId))
        .thenReturn(ticketDto);

    mockMvc
        .perform(
            get(CONTROLLER_URL + "/" + ticketId)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(ticketId.intValue())))
        .andExpect(jsonPath("$.name", is("Mega important ticket")))
        .andExpect(jsonPath("$.createdOn", is("21/05/2020")))
        .andExpect(jsonPath("$.state", is(APPROVED.name())))
        .andExpect(jsonPath("$.category.id", is(1)))
        .andExpect(jsonPath("$.category.name", is("Utilization")))
        .andExpect(jsonPath("$.urgency", is(CRITICAL.name())))
        .andExpect(jsonPath("$.description", is("Super mega important description")))
        .andExpect(jsonPath("$.desiredResolutionDate", is("06/06/2020")))
        .andExpect(jsonPath("$.owner.firstName", is("Brandon")))
        .andExpect(jsonPath("$.owner.lastName", is("Spirit")))
        .andExpect(jsonPath("$.actions", is(List.of(ASSIGN_TO_ME.name(), DECLINE.name()))))
        .andExpect(jsonPath("$.assignee.firstName", is("Alex")))
        .andExpect(jsonPath("$.assignee.lastName", is("Old")))
        .andExpect(jsonPath("$.approver.firstName", is("Oleg")))
        .andExpect(jsonPath("$.approver.lastName", is("Permgen")));

    verify(ticketService, times(1))
        .findById(ticketId);

    verifyNoMoreInteractions(
        ticketService
    );
  }

  @Test
  void testCreate() throws Exception {
    Category category = Category.builder()
        .id(4L)
        .name("Call me")
        .build();

    final TicketDto ticketDto = TicketDto.builder()
        .name("PC broke, pls fix")
        .description("Very urgent pls")
        .state(CANCELED)
        .category(category)
        .urgency(HIGH)
        .build();

    final Ticket ticket = Ticket.builder()
        .id(5L)
        .build();

    when(ticketService.save(ticketDto))
        .thenReturn(ticket);

    mockMvc
        .perform(post(CONTROLLER_URL).flashAttr("ticketDto", ticketDto))
        .andExpect(status().isCreated());

    verify(ticketService, times(1))
        .save(ticketDto);

    verifyNoMoreInteractions(
        ticketService
    );
  }

  @Test
  void testEdit() throws Exception {
    final Long ticketId = 6L;

    final TicketDto ticketDto = TicketDto.builder()
        .name("PC broke, pls fix x2")
        .description("Very urgent pls x2")
        .state(DECLINED)
        .category(Category.builder().id(4L).name("Call me x2").build())
        .urgency(CRITICAL)
        .build();

    mockMvc
        .perform(
            put(CONTROLLER_URL + "/" + ticketId)
                .flashAttr("ticketDto", ticketDto))
        .andExpect(status().isOk());

    verify(ticketService, times(1))
        .edit(ticketId, ticketDto);

    verifyNoMoreInteractions(
        ticketService
    );
  }

  @Test
  void testTransitState() throws Exception {
    final long ticketId = 9L;
    final Action action = Action.APPROVE;

    mockMvc
        .perform(
            put(CONTROLLER_URL + "/" + ticketId + "/action")
                .param("action", action.name()))
        .andExpect(status().isOk());

    verify(ticketService, times(1))
        .transitState(ticketId, action);

    verifyNoMoreInteractions(
        ticketService
    );
  }
}
