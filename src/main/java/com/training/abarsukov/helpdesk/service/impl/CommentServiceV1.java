package com.training.abarsukov.helpdesk.service.impl;

import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

import com.training.abarsukov.helpdesk.converter.CommentConverter;
import com.training.abarsukov.helpdesk.dto.CommentDto;
import com.training.abarsukov.helpdesk.model.Comment;
import com.training.abarsukov.helpdesk.model.Ticket;
import com.training.abarsukov.helpdesk.repository.CommentRepository;
import com.training.abarsukov.helpdesk.security.UserService;
import com.training.abarsukov.helpdesk.service.CommentService;
import com.training.abarsukov.helpdesk.service.TicketAccessHandler;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentServiceV1 implements CommentService {

  private final UserService userService;

  private final CommentRepository commentRepository;

  private final CommentConverter commentConverter;

  private final TicketAccessHandler ticketAccessHandler;

  @Override
  @Transactional
  public Comment save(Long ticketId, CommentDto commentDto) {
    ticketAccessHandler.findByIdToView(ticketId);

    final Comment comment = commentConverter.convertToEntity(commentDto);

    comment.setUser(userService.getUser());
    comment.setTicket(Ticket.builder().id(ticketId).build());
    comment.setDate(Timestamp.valueOf(LocalDateTime.now()));

    return commentRepository.save(comment);
  }

  @Override
  @Transactional(propagation = SUPPORTS, readOnly = true)
  public List<CommentDto> findByTicketId(Long id, Boolean doGetAll) {
    final List<Comment> comments = commentRepository
        .findByTicketId(id, doGetAll, userService.getUser());
    return comments.stream()
        .map(commentConverter::convertToDto)
        .collect(Collectors.toList());
  }
}
