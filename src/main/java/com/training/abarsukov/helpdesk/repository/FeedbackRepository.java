package com.training.abarsukov.helpdesk.repository;

import com.training.abarsukov.helpdesk.model.Feedback;
import com.training.abarsukov.helpdesk.model.User;
import com.training.abarsukov.helpdesk.repository.generic.Repository;

import java.util.Optional;

public interface FeedbackRepository extends Repository<Feedback, Long> {
  Optional<Feedback> findByTicketId(Long ticketId, User user);
}
