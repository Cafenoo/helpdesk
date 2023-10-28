package com.training.abarsukov.helpdesk.service.impl;

import com.training.abarsukov.helpdesk.converter.FeedbackConverter;
import com.training.abarsukov.helpdesk.dto.FeedbackDto;
import com.training.abarsukov.helpdesk.model.Feedback;
import com.training.abarsukov.helpdesk.model.Ticket;
import com.training.abarsukov.helpdesk.repository.FeedbackRepository;
import com.training.abarsukov.helpdesk.security.UserService;
import com.training.abarsukov.helpdesk.service.EmailService;
import com.training.abarsukov.helpdesk.service.FeedbackService;
import com.training.abarsukov.helpdesk.service.TicketAccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.sql.Date;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class FeedbackServiceV1 implements FeedbackService {

  private final UserService userService;

  private final TicketAccessHandler ticketAccessHandler;

  private final FeedbackRepository feedbackRepository;

  private final FeedbackConverter feedbackConverter;

  private final EmailService emailService;

  @Override
  @Transactional
  public Feedback save(Long ticketId, FeedbackDto feedbackDto) {
    final Ticket ticket = ticketAccessHandler.findByIdToSaveFeedback(ticketId);

    final Feedback feedback = feedbackConverter.convertToEntity(feedbackDto);
    feedback.setTicket(ticket);
    feedback.setUser(userService.getUser());
    feedback.setDate(Date.valueOf(LocalDate.now()));

    final Feedback savedFeedback = feedbackRepository.save(feedback);

    emailService.sendMailThatTicketHasGotFeedback(ticket);

    return savedFeedback;
  }

  @Override
  public FeedbackDto findByTicketId(Long id) {
    final Feedback feedback =
        feedbackRepository
            .findByTicketId(id, userService.getUser())
            .orElseThrow(EntityNotFoundException::new);
    return feedbackConverter.convertToDto(feedback);
  }
}
