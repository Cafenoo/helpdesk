package com.training.abarsukov.helpdesk.controller;

import com.training.abarsukov.helpdesk.dto.FeedbackDto;
import com.training.abarsukov.helpdesk.model.Feedback;
import com.training.abarsukov.helpdesk.service.FeedbackService;
import java.net.URI;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
public class FeedbackController {

  private final FeedbackService feedbackService;

  @PostMapping("/{ticketId}/feedback")
  public ResponseEntity<Void> createFeedback(
      @PathVariable Long ticketId, 
      @Valid @RequestBody FeedbackDto feedbackDto) {
    final Feedback savedFeedback = feedbackService.save(ticketId, feedbackDto);
    URI uri = URI.create("/tickets/" + ticketId + "/feedback/" + savedFeedback.getId());
    return ResponseEntity.created(uri)
        .build();
  }

  @GetMapping("/{ticketId}/feedback")
  public ResponseEntity<FeedbackDto> getFeedback(
      @PathVariable Long ticketId) {
    final FeedbackDto feedbackDto = feedbackService.findByTicketId(ticketId);
    return ResponseEntity.ok(feedbackDto);
  }
}
