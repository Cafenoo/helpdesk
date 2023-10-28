package com.training.abarsukov.helpdesk.repository.impl;

import com.training.abarsukov.helpdesk.model.Feedback;
import com.training.abarsukov.helpdesk.model.User;
import com.training.abarsukov.helpdesk.repository.FeedbackRepository;
import com.training.abarsukov.helpdesk.repository.generic.abstracts.AbstractRepository;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;

@Repository
public class FeedbackRepositoryV1 extends AbstractRepository<Feedback, Long>
    implements FeedbackRepository {

  private static final String GET_FEEDBACK_BY_TICKET_ID =
      "from Feedback f" + " left join fetch f.ticket t" + " where f.ticket.id = :ticket_id";

  @Override
  public Optional<Feedback> findByTicketId(Long ticketId, User user) {
    final String conditionToGetTicketsByRole =
        TicketRepositoryV1.Queries.MAP_OF_CONDITIONS_TO_GET_TICKETS.get(user.getRole());

    final Map<String, Object> parameters =
        Map.of(
            TicketRepositoryV1.Parameters.TICKET_ID,
            ticketId,
            TicketRepositoryV1.Parameters.USER_ID,
            user.getId());

    final String query = GET_FEEDBACK_BY_TICKET_ID + AND + conditionToGetTicketsByRole;

    return findByParameters(query, parameters).stream().findFirst();
  }
}
