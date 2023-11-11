package com.training.abarsukov.helpdesk.repository.impl;

import com.training.abarsukov.helpdesk.model.History;
import com.training.abarsukov.helpdesk.model.User;
import com.training.abarsukov.helpdesk.model.enums.Role;
import com.training.abarsukov.helpdesk.repository.HistoryRepository;
import com.training.abarsukov.helpdesk.repository.generic.abstracts.AbstractRepository;
import com.training.abarsukov.helpdesk.repository.impl.TicketRepositoryV1.Queries;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import org.springframework.stereotype.Repository;

@Repository
public class HistoryRepositoryV1
    extends AbstractRepository<History, Long>
    implements HistoryRepository {

  private static final int FIRST_RESULT = 0;

  private static final int NUMBER_OF_HISTORY_ROWS_ON_PAGE = 5;

  private static final String GET_HISTORY_BY_TICKET_ID =
      "from History h"
          + " left join fetch h.ticket t"
          + " left join fetch h.user"
          + " where h.ticket.id = :ticket_id";

  private static final String REVERSE_ORDER =
      " order by h.id desc";

  @Override
  public List<History> findByTicketId(Long ticketId, boolean doGetAll, User user) {
    final Map<Role, String> conditionsMap = Queries.MAP_OF_CONDITIONS_TO_GET_TICKETS;
    final String conditionToGetTicketsByRole = conditionsMap.get(user.getRole());

    final Map<String, Object> parameters = Map.of(
        TicketRepositoryV1.Parameters.TICKET_ID, ticketId,
        TicketRepositoryV1.Parameters.USER_ID, user.getId());

    final String query = GET_HISTORY_BY_TICKET_ID + AND + conditionToGetTicketsByRole;

    final Map<Boolean, Supplier<List<History>>> repositoryMethodsMap = Map.of(
        true, () -> findByParameters(query, parameters),
        false, () -> findByParameters(
            query + REVERSE_ORDER,
            parameters,
            FIRST_RESULT,
            NUMBER_OF_HISTORY_ROWS_ON_PAGE));

    return repositoryMethodsMap.get(doGetAll)
        .get();
  }
}
