package com.training.abarsukov.helpdesk.service.impl;

import static java.util.Optional.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.training.abarsukov.helpdesk.converter.TicketConverter;
import com.training.abarsukov.helpdesk.dto.AttachmentDto;
import com.training.abarsukov.helpdesk.dto.CommentDto;
import com.training.abarsukov.helpdesk.dto.TicketDto;
import com.training.abarsukov.helpdesk.dto.UserDto;
import com.training.abarsukov.helpdesk.exception.TicketStateException;
import com.training.abarsukov.helpdesk.model.Category;
import com.training.abarsukov.helpdesk.model.Ticket;
import com.training.abarsukov.helpdesk.model.User;
import com.training.abarsukov.helpdesk.model.enums.Action;
import com.training.abarsukov.helpdesk.model.enums.SortingField;
import com.training.abarsukov.helpdesk.model.enums.State;
import com.training.abarsukov.helpdesk.model.enums.Urgency;
import com.training.abarsukov.helpdesk.repository.TicketRepository;
import com.training.abarsukov.helpdesk.security.UserService;
import com.training.abarsukov.helpdesk.service.ActionHandler;
import com.training.abarsukov.helpdesk.service.AttachmentService;
import com.training.abarsukov.helpdesk.service.CommentService;
import com.training.abarsukov.helpdesk.service.EmailService;
import com.training.abarsukov.helpdesk.service.HistoryService;
import java.sql.Date;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TicketServiceV1Test {

  private static final long ID_LITERAL = 5L;
  private static final String NAME_LITERAL = "name";
  private static final long CATEGORY_ID_LITERAL = 1L;
  private static final String CATEGORY_NAME_LITERAL = "Category";
  private static final String CREATED_ON_LITERAL = "2020-02-02";
  private static final String DESCRIPTION_LITERAL = "Description";
  private static final String DESIRED_RESOLUTION_DATE_LITERAL = "2020-06-21";
  private static final String USER_FIRST_NAME_LITERAL = "Alex";
  private static final String USER_LAST_NAME_LITERAL = "Spirit";
  private static final long ATTACHMENT_ID_LITERAL = 2L;
  private static final String COMMENT_TEXT_LITERAL = "Comment text";

  @InjectMocks
  private TicketServiceV1 ticketService;

  @Mock
  private UserService userService;

  @Mock
  private TicketRepository ticketRepository;

  @Mock
  private TicketConverter ticketConverter;

  @Mock
  private CommentService commentService;

  @Mock
  private HistoryService historyService;

  @Mock
  private AttachmentService attachmentService;

  @Mock
  private ActionHandler actionHandler;

  @Mock
  private EmailService emailService;

  private Long id;
  private String name;
  private Date createdOn;
  private State state;
  private Category category;
  private Urgency urgency;
  private String description;
  private Date desiredResolutionDate;
  private User user;
  private UserDto userDto;
  private Action action;
  private Ticket ticket;
  private TicketDto ticketDto;
  private AttachmentDto attachmentDto;
  private CommentDto commentDto;

  @BeforeEach
  void setUp() {
    id = ID_LITERAL;
    name = NAME_LITERAL;
    createdOn = Date.valueOf(CREATED_ON_LITERAL);
    state = State.DRAFT;
    category = Category.builder()
        .id(CATEGORY_ID_LITERAL)
        .name(CATEGORY_NAME_LITERAL)
        .build();
    urgency = Urgency.HIGH;
    description = DESCRIPTION_LITERAL;
    desiredResolutionDate = Date.valueOf(DESIRED_RESOLUTION_DATE_LITERAL);
    user = User.builder()
        .firstName(USER_FIRST_NAME_LITERAL)
        .lastName(USER_LAST_NAME_LITERAL)
        .build();
    userDto = UserDto.builder()
        .firstName(USER_FIRST_NAME_LITERAL)
        .lastName(USER_LAST_NAME_LITERAL)
        .build();
    action = Action.APPROVE;
    ticket = Ticket.builder()
        .id(id)
        .name(name)
        .createdOn(createdOn)
        .state(state)
        .category(category)
        .urgency(urgency)
        .description(description)
        .desiredResolutionDate(desiredResolutionDate)
        .owner(user)
        .build();
    ticketDto = TicketDto.builder()
        .id(id)
        .name(name)
        .createdOn(createdOn)
        .state(state)
        .category(category)
        .urgency(urgency)
        .description(description)
        .desiredResolutionDate(desiredResolutionDate)
        .owner(userDto)
        .build();
    attachmentDto = AttachmentDto.builder()
        .id(ATTACHMENT_ID_LITERAL)
        .build();
    commentDto = CommentDto.builder()
        .text(COMMENT_TEXT_LITERAL)
        .build();
  }

  @Test
  void testFindAll() {
    final Integer page = 1;
    final Integer pageSize = 10;
    final SortingField sortingField = SortingField.DEFAULT;
    final String keyword = "keyword";
    final Boolean isPersonal = false;

    final List<Ticket> ticketList = List.of(ticket, ticket, ticket);
    final List<TicketDto> ticketDtoList = List.of(ticketDto, ticketDto, ticketDto);

    when(userService.getUser())
        .thenReturn(user);
    when(ticketRepository.findAll(page, pageSize, isPersonal, keyword, user))
        .thenReturn(ticketList);
    when(ticketConverter.convertToTicketListDto(any()))
        .thenReturn(ticketDto);

    final List<TicketDto> actualTicketDtoList = ticketService.findAll(
        page, 
        pageSize, 
        sortingField, 
        keyword, 
        isPersonal);

    assertEquals(ticketDtoList, actualTicketDtoList);

    verify(userService, times(1))
        .getUser();
    verify(ticketRepository, times(1))
        .findAll(page, pageSize, isPersonal, keyword, user);
    verify(ticketConverter, times(3))
        .convertToTicketListDto(ticket);
    verify(actionHandler, times(3))
        .getPossibleActions(ticketDto.getState());

    verifyNoMoreInteractions(
        userService,
        ticketRepository,
        ticketConverter,
        actionHandler);
    verifyNoInteractions(
        commentService,
        historyService,
        attachmentService,
        emailService);
  }

  @Test
  void testFindById() {
    when(userService.getUser())
        .thenReturn(user);
    when(ticketRepository.findById(id, user))
        .thenReturn(of(ticket));
    when(ticketConverter.convertToDto(ticket))
        .thenReturn(ticketDto);
    when(actionHandler.getPossibleActions(state))
        .thenReturn(List.of(Action.ASSIGN_TO_ME, Action.CANCEL));

    final TicketDto actualTicketDto = ticketService.findById(id);

    assertEquals(id, actualTicketDto.getId());
    assertEquals(name, actualTicketDto.getName());
    assertEquals(createdOn, actualTicketDto.getCreatedOn());
    assertEquals(state, actualTicketDto.getState());
    assertEquals(category, actualTicketDto.getCategory());
    assertEquals(urgency, actualTicketDto.getUrgency());
    assertEquals(description, actualTicketDto.getDescription());
    assertEquals(desiredResolutionDate, actualTicketDto.getDesiredResolutionDate());
    assertEquals(userDto, actualTicketDto.getOwner());

    verify(userService, times(1))
        .getUser();
    verify(ticketRepository, times(1))
        .findById(id, user);
    verify(ticketConverter, times(1))
        .convertToDto(ticket);
    verify(actionHandler, times(1))
        .getPossibleActions(state);

    verifyNoMoreInteractions(
        userService,
        ticketRepository,
        ticketConverter,
        actionHandler);
    verifyNoInteractions(
        commentService,
        historyService,
        attachmentService,
        emailService);
  }

  @Test
  void testSave() {
    when(ticketConverter.convertToEntity(ticketDto))
        .thenReturn(ticket);
    when(userService.getUser())
        .thenReturn(user);
    when(ticketRepository.save(ticket))
        .thenReturn(ticket);

    final Ticket result = ticketService.save(ticketDto);

    assertEquals(ticket.getId(), result.getId());
    assertEquals(ticket.getName(), result.getName());
    assertEquals(ticket.getCreatedOn(), result.getCreatedOn());
    assertEquals(ticket.getState(), result.getState());
    assertEquals(ticket.getCategory(), result.getCategory());
    assertEquals(ticket.getUrgency(), result.getUrgency());
    assertEquals(ticket.getDescription(), result.getDescription());
    assertEquals(ticket.getDesiredResolutionDate(), result.getDesiredResolutionDate());
    assertEquals(ticket.getOwner(), result.getOwner());

    verify(ticketConverter, times(1))
        .convertToEntity(ticketDto);
    verify(userService, times(1))
        .getUser();
    verify(ticketRepository, times(1))
        .save(ticket);
    verify(historyService, times(1))
        .save(any(), any(), any());

    verifyNoMoreInteractions(
        ticketConverter,
        userService,
        ticketRepository,
        historyService);
    verifyNoInteractions(
        commentService,
        attachmentService,
        emailService,
        actionHandler);
  }

  @Test
  void testSaveWithComment() {
    ticketDto.setComment(commentDto);

    when(ticketConverter.convertToEntity(ticketDto))
        .thenReturn(ticket);
    when(userService.getUser())
        .thenReturn(user);
    when(ticketRepository.save(ticket))
        .thenReturn(ticket);

    final Ticket result = ticketService.save(ticketDto);

    assertEquals(ticket.getId(), result.getId());
    assertEquals(ticket.getName(), result.getName());
    assertEquals(ticket.getCreatedOn(), result.getCreatedOn());
    assertEquals(ticket.getState(), result.getState());
    assertEquals(ticket.getCategory(), result.getCategory());
    assertEquals(ticket.getUrgency(), result.getUrgency());
    assertEquals(ticket.getDescription(), result.getDescription());
    assertEquals(ticket.getDesiredResolutionDate(), result.getDesiredResolutionDate());
    assertEquals(ticket.getOwner(), result.getOwner());

    verify(ticketConverter, times(1))
        .convertToEntity(ticketDto);
    verify(userService, times(1))
        .getUser();
    verify(ticketRepository, times(1))
        .save(ticket);
    verify(historyService, times(1))
        .save(any(), any(), any());
    verify(commentService, times(1))
        .save(ticket.getId(), commentDto);

    verifyNoMoreInteractions(
        ticketConverter,
        userService,
        ticketRepository,
        historyService);
    verifyNoInteractions(
        attachmentService,
        emailService,
        actionHandler);
  }

  @Test
  void testSaveWithAttachment() {
    ticketDto.setAttachment(attachmentDto);

    when(ticketConverter.convertToEntity(ticketDto))
        .thenReturn(ticket);
    when(userService.getUser())
        .thenReturn(user);
    when(ticketRepository.save(ticket))
        .thenReturn(ticket);

    final Ticket result = ticketService.save(ticketDto);

    assertEquals(ticket.getId(), result.getId());
    assertEquals(ticket.getName(), result.getName());
    assertEquals(ticket.getCreatedOn(), result.getCreatedOn());
    assertEquals(ticket.getState(), result.getState());
    assertEquals(ticket.getCategory(), result.getCategory());
    assertEquals(ticket.getUrgency(), result.getUrgency());
    assertEquals(ticket.getDescription(), result.getDescription());
    assertEquals(ticket.getDesiredResolutionDate(), result.getDesiredResolutionDate());
    assertEquals(ticket.getOwner(), result.getOwner());

    verify(ticketConverter, times(1))
        .convertToEntity(ticketDto);
    verify(userService, times(1))
        .getUser();
    verify(ticketRepository, times(1))
        .save(ticket);
    verify(historyService, times(1))
        .save(any(), any(), any());
    verify(attachmentService, times(1))
        .save(ticket.getId(), attachmentDto);

    verifyNoMoreInteractions(
        ticketConverter,
        userService,
        ticketRepository,
        historyService);
    verifyNoInteractions(
        commentService,
        emailService,
        actionHandler);
  }

  @Test
  void testSaveWithStateNew() {
    ticketDto.setState(State.NEW);

    when(ticketConverter.convertToEntity(ticketDto))
        .thenReturn(ticket);
    when(userService.getUser())
        .thenReturn(user);
    when(ticketRepository.save(ticket))
        .thenReturn(ticket);

    final Ticket result = ticketService.save(ticketDto);

    assertEquals(ticket.getId(), result.getId());
    assertEquals(ticket.getName(), result.getName());
    assertEquals(ticket.getCreatedOn(), result.getCreatedOn());
    assertEquals(ticket.getState(), result.getState());
    assertEquals(ticket.getCategory(), result.getCategory());
    assertEquals(ticket.getUrgency(), result.getUrgency());
    assertEquals(ticket.getDescription(), result.getDescription());
    assertEquals(ticket.getDesiredResolutionDate(), result.getDesiredResolutionDate());
    assertEquals(ticket.getOwner(), result.getOwner());

    verify(ticketConverter, times(1))
        .convertToEntity(ticketDto);
    verify(userService, times(1))
        .getUser();
    verify(ticketRepository, times(1))
        .save(ticket);
    verify(historyService, times(1))
        .save(any(), any(), any());
    verify(emailService, times(1))
        .sendMailThatTicketHasToBeApproved(ticket);

    verifyNoMoreInteractions(
        ticketConverter,
        userService,
        ticketRepository,
        historyService);
    verifyNoInteractions(
        commentService,
        attachmentService,
        actionHandler);
  }

  @Test
  void testSaveWithInvalidState() {
    ticketDto.setState(State.DECLINED);

    assertThrows(TicketStateException.class,
        () -> ticketService.save(ticketDto));

    verifyNoInteractions(
        ticketConverter,
        userService,
        ticketRepository,
        historyService,
        commentService,
        attachmentService,
        emailService,
        actionHandler);
  }

  @Test
  void testEdit() {
    when(userService.getUser())
        .thenReturn(user);
    when(ticketRepository.findByIdToEditTicket(id, user))
        .thenReturn(of(ticket));
    when(ticketConverter.convertToEntity(ticketDto))
        .thenReturn(ticket);

    ticketService.edit(id, ticketDto);

    verify(userService, times(1))
        .getUser();
    verify(ticketRepository, times(1))
        .findByIdToEditTicket(id, user);
    verify(ticketConverter, times(1))
        .convertToEntity(ticketDto);
    verify(ticketRepository, times(1))
        .update(ticket);
    verify(historyService, times(1))
        .save(any(), any(), any());

    verifyNoMoreInteractions(
        ticketConverter,
        ticketRepository,
        historyService,
        userService);
    verifyNoInteractions(
        commentService,
        attachmentService,
        actionHandler,
        emailService);
  }

  @Test
  void testEditWithAttachment() {
    ticketDto.setAttachment(attachmentDto);

    when(userService.getUser())
        .thenReturn(user);
    when(ticketRepository.findByIdToEditTicket(id, user))
        .thenReturn(of(ticket));
    when(ticketConverter.convertToEntity(ticketDto))
        .thenReturn(ticket);

    ticketService.edit(id, ticketDto);

    verify(userService, times(1))
        .getUser();
    verify(ticketRepository, times(1))
        .findByIdToEditTicket(id, user);
    verify(ticketConverter, times(1))
        .convertToEntity(ticketDto);
    verify(ticketRepository, times(1))
        .update(ticket);
    verify(attachmentService, times(1))
        .edit(ticketDto.getId(), ticketDto.getAttachment());
    verify(historyService, times(1))
        .save(any(), any(), any());

    verifyNoMoreInteractions(
        ticketConverter,
        ticketRepository,
        historyService,
        userService);
    verifyNoInteractions(
        commentService,
        actionHandler,
        emailService);
  }

  @Test
  void testEditWhenTicketIsNotInDraftOrNew() {
    ticketDto.setState(State.APPROVED);

    when(userService.getUser())
        .thenReturn(user);
    when(ticketRepository.findByIdToEditTicket(id, user))
        .thenReturn(of(ticket));
    when(ticketConverter.convertToEntity(ticketDto))
        .thenReturn(ticket);

    assertThrows(TicketStateException.class,
        () -> ticketService.edit(id, ticketDto));

    verify(userService, times(1))
        .getUser();
    verify(ticketRepository, times(1))
        .findByIdToEditTicket(id, user);
    verify(ticketConverter, times(1))
        .convertToEntity(ticketDto);

    verifyNoMoreInteractions(
        ticketConverter,
        historyService,
        userService,
        ticketRepository);
    verifyNoInteractions(
        commentService,
        attachmentService,
        actionHandler,
        historyService);
  }

  @Test
  void testEditWithStateNew() {
    ticketDto.setState(State.NEW);

    when(userService.getUser())
        .thenReturn(user);
    when(ticketRepository.findByIdToEditTicket(id, user))
        .thenReturn(of(ticket));
    when(ticketConverter.convertToEntity(ticketDto))
        .thenReturn(ticket);

    ticketService.edit(id, ticketDto);

    verify(userService, times(1))
        .getUser();
    verify(ticketRepository, times(1))
        .findByIdToEditTicket(id, user);
    verify(ticketConverter, times(1))
        .convertToEntity(ticketDto);
    verify(ticketRepository, times(1))
        .update(ticket);
    verify(historyService, times(1))
        .save(any(), any(), any());

    verifyNoMoreInteractions(
        ticketConverter,
        ticketRepository,
        historyService,
        userService);
    verifyNoInteractions(
        commentService,
        actionHandler,
        attachmentService);
  }

  @Test
  void testTransitState() {
    when(userService.getUser())
        .thenReturn(user);
    when(ticketRepository.findById(id, user))
        .thenReturn(of(ticket));

    ticketService.transitState(id, action);

    verify(userService, times(1))
        .getUser();
    verify(ticketRepository, times(1))
        .findById(id, user);
    verify(actionHandler, times(1))
        .transitState(ticket, action);
    verify(ticketRepository, times(1))
        .update(ticket);
    verify(historyService, times(1))
        .save(any(), any(), any());

    verifyNoMoreInteractions(
        userService,
        ticketRepository,
        actionHandler,
        ticketRepository,
        historyService);
    verifyNoInteractions(
        ticketConverter,
        commentService,
        attachmentService,
        emailService);
  }
}
