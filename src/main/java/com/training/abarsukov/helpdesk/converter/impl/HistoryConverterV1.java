package com.training.abarsukov.helpdesk.converter.impl;

import com.training.abarsukov.helpdesk.converter.HistoryConverter;
import com.training.abarsukov.helpdesk.converter.UserConverter;
import com.training.abarsukov.helpdesk.dto.HistoryDto;
import com.training.abarsukov.helpdesk.model.History;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HistoryConverterV1 implements HistoryConverter {

  private final UserConverter userConverter;

  @Override
  public HistoryDto convertToDto(History history) {
    return HistoryDto.builder()
        .timestamp(history.getDate())
        .user(userConverter.convertToDto(history.getUser()))
        .action(history.getAction())
        .description(history.getDescription())
        .build();
  }
}
