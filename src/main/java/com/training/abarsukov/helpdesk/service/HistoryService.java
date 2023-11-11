package com.training.abarsukov.helpdesk.service;

import com.training.abarsukov.helpdesk.dto.HistoryDto;
import java.util.List;

public interface HistoryService {

  List<HistoryDto> findByTicketId(Long ticketId, Boolean doGetAll);

  void save(Long ticketId, String action, String description);
}
