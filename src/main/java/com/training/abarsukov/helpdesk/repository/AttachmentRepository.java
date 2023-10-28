package com.training.abarsukov.helpdesk.repository;

import com.training.abarsukov.helpdesk.model.Attachment;
import com.training.abarsukov.helpdesk.model.User;
import com.training.abarsukov.helpdesk.repository.generic.Repository;

import java.util.Optional;

public interface AttachmentRepository extends Repository<Attachment, Long> {
  Optional<Attachment> findByTicketId(Long tickedId, User user);

  void deleteByTicketId(Long ticketId);
}
