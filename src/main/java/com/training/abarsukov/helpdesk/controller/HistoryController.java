package com.training.abarsukov.helpdesk.controller;

import static java.util.Optional.ofNullable;

import com.training.abarsukov.helpdesk.dto.HistoryDto;
import com.training.abarsukov.helpdesk.service.HistoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
public class HistoryController {

  private final HistoryService historyService;

  @GetMapping("/{ticketId}/history")
  public ResponseEntity<List<HistoryDto>> getHistory(
      @PathVariable Long ticketId,
      @RequestParam(required = false) Boolean doGetAll) {
    final Boolean processedDoGetAll = ofNullable(doGetAll).orElse(false);

    final List<HistoryDto> historyDtoList =
        historyService.findByTicketId(ticketId, processedDoGetAll);
    return ResponseEntity.ok(historyDtoList);
  }
}
