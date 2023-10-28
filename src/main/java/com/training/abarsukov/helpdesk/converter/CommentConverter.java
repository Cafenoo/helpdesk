package com.training.abarsukov.helpdesk.converter;

import com.training.abarsukov.helpdesk.dto.CommentDto;
import com.training.abarsukov.helpdesk.model.Comment;

public interface CommentConverter {
  CommentDto convertToDto(Comment comment);

  Comment convertToEntity(CommentDto commentDto);
}
