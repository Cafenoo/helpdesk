package com.training.abarsukov.helpdesk.repository.impl;

import com.training.abarsukov.helpdesk.model.Attachment;
import com.training.abarsukov.helpdesk.model.User;
import com.training.abarsukov.helpdesk.model.enums.Role;
import com.training.abarsukov.helpdesk.repository.AttachmentRepository;
import com.training.abarsukov.helpdesk.repository.generic.abstracts.AbstractRepository;
import com.training.abarsukov.helpdesk.repository.impl.TicketRepositoryV1.Queries;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class AttachmentRepositoryV1
    extends AbstractRepository<Attachment, Long>
    implements AttachmentRepository {

  private static final String SELECT_ATTACHMENT =
      "from Attachment a"
          + " left join fetch a.ticket t"
          + " left join fetch t.owner";

  private static final String DELETE_ATTACHMENT =
      "delete from Attachment a";

  private static final String BY_TICKET_ID =
      " where a.ticket.id = :ticket_id";

  @Override
  public Optional<Attachment> findByTicketId(Long ticketId, User user) {
    final Map<Role, String> conditionsMap = Queries.MAP_OF_CONDITIONS_TO_GET_TICKETS;
    final String conditionToGetTicketsByRole = conditionsMap.get(user.getRole());

    final Map<String, Object> parameters = Map.of(
        TicketRepositoryV1.Parameters.TICKET_ID, ticketId,
        TicketRepositoryV1.Parameters.USER_ID, user.getId());

    final String query = SELECT_ATTACHMENT + BY_TICKET_ID + AND + conditionToGetTicketsByRole;

    return findByParameters(query, parameters).stream().findFirst();
  }

  @Override
  public void deleteByTicketId(Long ticketId) {
    final Map<String, Object> parameters = Map.of(
        TicketRepositoryV1.Parameters.TICKET_ID, ticketId);

    final String query = DELETE_ATTACHMENT + BY_TICKET_ID;

    updateByParameters(query, parameters);
  }
}
