package com.training.abarsukov.helpdesk.service;

import com.training.abarsukov.helpdesk.dto.FeedbackDto;
import com.training.abarsukov.helpdesk.model.Feedback;

public interface FeedbackService {
  Feedback save(Long ticketId, FeedbackDto feedbackDto);

  FeedbackDto findByTicketId(Long ticketId);
}
