package com.training.abarsukov.helpdesk.controller;

import com.training.abarsukov.helpdesk.model.enums.Urgency;
import com.training.abarsukov.helpdesk.service.UrgencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/urgency")
@RequiredArgsConstructor
public class UrgencyController {

  private final UrgencyService urgencyService;

  @GetMapping
  public ResponseEntity<List<Urgency>> getUrgencyList() {
    final List<Urgency> urgencyList = urgencyService.findAll();
    return ResponseEntity.ok(urgencyList);
  }
}
