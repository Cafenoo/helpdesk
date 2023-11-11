package com.training.abarsukov.helpdesk.repository.impl;

import com.training.abarsukov.helpdesk.model.Comment;
import com.training.abarsukov.helpdesk.model.User;
import com.training.abarsukov.helpdesk.model.enums.Role;
import com.training.abarsukov.helpdesk.repository.CommentRepository;
import com.training.abarsukov.helpdesk.repository.generic.abstracts.AbstractRepository;
import com.training.abarsukov.helpdesk.repository.impl.TicketRepositoryV1.Queries;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import org.springframework.stereotype.Repository;

@Repository
public class CommentRepositoryV1
    extends AbstractRepository<Comment, Long>
    implements CommentRepository {

  private static final int FIRST_RESULT = 0;

  private static final int NUMBER_OF_COMMENTS_ON_PAGE = 5;

  private static final String GET_COMMENTS_BY_TICKET_ID =
      "from Comment c"
          + " left join fetch c.ticket t"
          + " left join fetch c.user"
          + " where c.ticket.id = :ticket_id";

  private static final String REVERSE_ORDER =
      " order by c.id desc";

  @Override
  public List<Comment> findByTicketId(Long ticketId, boolean doGetAll, User user) {
    final Map<Role, String> conditionsMap = Queries.MAP_OF_CONDITIONS_TO_GET_TICKETS;
    final String conditionToGetTicketsByRole = conditionsMap.get(user.getRole());

    final Map<String, Object> parameters = Map.of(
        TicketRepositoryV1.Parameters.TICKET_ID, ticketId,
        TicketRepositoryV1.Parameters.USER_ID, user.getId());

    final String query = GET_COMMENTS_BY_TICKET_ID + AND + conditionToGetTicketsByRole;

    final Map<Boolean, Supplier<List<Comment>>> repositoryMethodsMap = Map.of(
        true, () -> findByParameters(query, parameters),
        false, () -> findByParameters(
            query + REVERSE_ORDER,
            parameters,
            FIRST_RESULT,
            NUMBER_OF_COMMENTS_ON_PAGE));

    return repositoryMethodsMap.get(doGetAll)
        .get();
  }
}
