package com.training.abarsukov.helpdesk.converter.impl;

import com.training.abarsukov.helpdesk.converter.FeedbackConverter;
import com.training.abarsukov.helpdesk.dto.FeedbackDto;
import com.training.abarsukov.helpdesk.model.Feedback;
import org.springframework.stereotype.Component;

@Component
public class FeedbackConverterV1 implements FeedbackConverter {

  @Override
  public Feedback convertToEntity(FeedbackDto feedbackDto) {
    return Feedback.builder()
        .rate(feedbackDto.getRate())
        .text(feedbackDto.getComment())
        .build();
  }

  @Override
  public FeedbackDto convertToDto(Feedback feedback) {
    return FeedbackDto.builder()
        .rate(feedback.getRate())
        .comment(feedback.getText())
        .build();
  }
}
