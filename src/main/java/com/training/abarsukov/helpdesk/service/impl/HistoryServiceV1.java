package com.training.abarsukov.helpdesk.service.impl;

import com.training.abarsukov.helpdesk.converter.HistoryConverter;
import com.training.abarsukov.helpdesk.dto.HistoryDto;
import com.training.abarsukov.helpdesk.model.History;
import com.training.abarsukov.helpdesk.model.Ticket;
import com.training.abarsukov.helpdesk.repository.HistoryRepository;
import com.training.abarsukov.helpdesk.security.UserService;
import com.training.abarsukov.helpdesk.service.HistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

@Service
@RequiredArgsConstructor
public class HistoryServiceV1 implements HistoryService {

  private final UserService userService;

  private final HistoryRepository historyRepository;

  private final HistoryConverter historyConverter;

  @Override
  @Transactional(propagation = SUPPORTS, readOnly = true)
  public List<HistoryDto> findByTicketId(Long ticketId, Boolean doGetAll) {
    final List<History> histories =
        historyRepository.findByTicketId(ticketId, doGetAll, userService.getUser());
    return histories.stream().map(historyConverter::convertToDto).collect(Collectors.toList());
  }

  @Override
  @Transactional
  public void save(Long ticketId, String action, String description) {
    final History history =
        History.builder()
            .ticket(Ticket.builder().id(ticketId).build())
            .date(Timestamp.valueOf(LocalDateTime.now()))
            .action(action)
            .description(description)
            .user(userService.getUser())
            .build();
    historyRepository.save(history);
  }
}
