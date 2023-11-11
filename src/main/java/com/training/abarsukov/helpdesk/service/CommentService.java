package com.training.abarsukov.helpdesk.service;

import com.training.abarsukov.helpdesk.dto.CommentDto;
import com.training.abarsukov.helpdesk.model.Comment;
import java.util.List;

public interface CommentService
{
  Comment save(Long ticketId, CommentDto commentDto);

  List<CommentDto> findByTicketId(Long id, Boolean doGetAll);
}
