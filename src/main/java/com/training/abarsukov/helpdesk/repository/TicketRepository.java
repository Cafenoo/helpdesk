package com.training.abarsukov.helpdesk.repository;

import com.training.abarsukov.helpdesk.model.Ticket;
import com.training.abarsukov.helpdesk.model.User;
import com.training.abarsukov.helpdesk.repository.generic.Repository;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends Repository<Ticket, Long> {
  List<Ticket> findAll(
      Integer page, Integer pageSize, Boolean isPersonal, String keyword, User user);

  Optional<Ticket> findById(Long id, User user);

  Optional<Ticket> findByIdToEditTicket(Long id, User user);

  Optional<Ticket> findByIdToSaveFeedback(Long id, User user);
}
