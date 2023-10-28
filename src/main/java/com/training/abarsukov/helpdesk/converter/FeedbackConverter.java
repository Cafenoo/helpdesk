package com.training.abarsukov.helpdesk.converter;

import com.training.abarsukov.helpdesk.dto.FeedbackDto;
import com.training.abarsukov.helpdesk.model.Feedback;

public interface FeedbackConverter {
  Feedback convertToEntity(FeedbackDto feedbackDto);

  FeedbackDto convertToDto(Feedback feedback);
}
