package com.training.abarsukov.helpdesk.converter.impl;

import com.training.abarsukov.helpdesk.converter.CommentConverter;
import com.training.abarsukov.helpdesk.converter.UserConverter;
import com.training.abarsukov.helpdesk.dto.CommentDto;
import com.training.abarsukov.helpdesk.model.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentConverterV1 implements CommentConverter {

  private final UserConverter userConverter;

  @Override
  public CommentDto convertToDto(Comment comment) {
    return CommentDto.builder()
        .timestamp(comment.getDate())
        .user(userConverter.convertToDto(comment.getUser()))
        .text(comment.getText())
        .build();
  }

  @Override
  public Comment convertToEntity(CommentDto commentDto) {
    return Comment.builder().text(commentDto.getText()).build();
  }
}
