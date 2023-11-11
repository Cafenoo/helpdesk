package com.training.abarsukov.helpdesk.repository.impl;

import static com.training.abarsukov.helpdesk.model.enums.Role.EMPLOYEE;
import static com.training.abarsukov.helpdesk.model.enums.Role.ENGINEER;
import static com.training.abarsukov.helpdesk.model.enums.Role.MANAGER;
import static com.training.abarsukov.helpdesk.model.enums.State.APPROVED;
import static com.training.abarsukov.helpdesk.model.enums.State.CANCELED;
import static com.training.abarsukov.helpdesk.model.enums.State.DECLINED;
import static com.training.abarsukov.helpdesk.model.enums.State.DONE;
import static com.training.abarsukov.helpdesk.model.enums.State.IN_PROGRESS;
import static com.training.abarsukov.helpdesk.model.enums.State.NEW;
import static java.text.MessageFormat.format;
import static java.util.Objects.nonNull;
import static lombok.AccessLevel.PRIVATE;

import com.training.abarsukov.helpdesk.model.Ticket;
import com.training.abarsukov.helpdesk.model.User;
import com.training.abarsukov.helpdesk.model.enums.Role;
import com.training.abarsukov.helpdesk.model.enums.State;
import com.training.abarsukov.helpdesk.repository.TicketRepository;
import com.training.abarsukov.helpdesk.repository.generic.abstracts.AbstractRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
public class TicketRepositoryV1
    extends AbstractRepository<Ticket, Long>
    implements TicketRepository {

  @Override
  public List<Ticket> findAll(
      Integer page,
      Integer pageSize,
      Boolean isPersonal,
      String keyword,
      User user) {
    final Map<Boolean, Supplier<String>> conditionsMap = Map.of(
        true, () -> Queries.MAP_OF_CONDITIONS_TO_GET_PERSONAL_TICKETS.get(user.getRole()),
        false, () -> Queries.MAP_OF_CONDITIONS_TO_GET_TICKETS.get(user.getRole()));
    final String conditionToGetTicketsByRole = conditionsMap.get(isPersonal).get();

    final Map<String, Object> parameters = new HashMap<>(
        Map.of(Parameters.USER_ID, user.getId()));

    String query = Queries.SELECT_TICKETS + WHERE + conditionToGetTicketsByRole;

    if (nonNull(keyword)) {
      query += AND + Queries.CONDITION_TO_SEARCH_BY_KEYWORD;
      parameters.put(Parameters.KEYWORD, keyword);
    }

    final int firstResult = (page - 1) * pageSize;

    return findByParameters(query, parameters, firstResult, pageSize);
  }

  @Override
  public Optional<Ticket> findById(Long id, User user) {
    final String query = Queries.SELECT_TICKETS
        + WHERE + Queries.CONDITION_TO_GET_TICKET_BY_ID
        + AND + Queries.MAP_OF_CONDITIONS_TO_GET_TICKETS.get(user.getRole());

    final Map<String, Object> parameters = Map.of(
        Parameters.TICKET_ID, id,
        Parameters.USER_ID, user.getId());

    return findByParameters(query, parameters).stream().findFirst();
  }

  @Override
  public Optional<Ticket> findByIdToEditTicket(Long id, User user) {
    final String query = Queries.SELECT_TICKETS
        + WHERE + Queries.CONDITION_TO_GET_TICKET_BY_ID
        + AND + Queries.CONDITION_TO_EDIT_TICKET;

    final Map<String, Object> parameters = Map.of(
        Parameters.TICKET_ID, id,
        Parameters.USER_ID, user.getId());

    return findByParameters(query, parameters).stream().findFirst();
  }

  @Override
  public Optional<Ticket> findByIdToSaveFeedback(Long id, User user) {
    final String query = Queries.SELECT_TICKETS
        + WHERE + Queries.CONDITION_TO_GET_TICKET_BY_ID
        + AND + Queries.MAP_OF_CONDITIONS_TO_GET_TICKETS.get(user.getRole())
        + AND + Queries.CONDITION_TO_SAVE_FEEDBACK;

    final Map<String, Object> parameters = Map.of(
        Parameters.TICKET_ID, id,
        Parameters.USER_ID, user.getId());

    return findByParameters(query, parameters).stream().findFirst();
  }

  @NoArgsConstructor(access = PRIVATE)
  public static final class Queries {

    private static final String SELECT_TICKETS =
        "from Ticket t"
            + " left join fetch t.assignee"
            + " left join fetch t.owner"
            + " left join fetch t.category"
            + " left join fetch t.approver";

    private static final String CONDITION_TO_GET_TICKET_BY_ID = format("(t.id = :{0})",
        Parameters.TICKET_ID);

    private static final String CONDITION_TO_SEARCH_BY_KEYWORD = format(
        "("
            + "cast(t.id as text) like {0}"
            + " or lower(t.name) like {0}"
            + " or function(''formatdatetime'', t.desiredResolutionDate, ''dd/MM/yyyy'') like {0}"
            + " or lower(t.urgency) like {0}"
            + " or lower(t.state) like {0}"
            + ")",
        "lower(concat('%', :" + Parameters.KEYWORD + ", '%'))");

    private static final String CONDITION_TO_GET_TICKETS_OF_EMPLOYEE = format(
        "(t.owner.id = :{0})",
        Parameters.USER_ID);

    private static final String CONDITION_TO_GET_TICKETS_OF_MANAGER = format(
        "(t.owner.id = :{7}"
            + " or t.owner.role = ''{0}''"
            + " and t.state = ''{1}''"
            + " or t.approver.id = :{7}"
            + " and t.state in(''{2}'', ''{3}'', ''{4}'', ''{5}'', ''{6}''))",
        EMPLOYEE, NEW, APPROVED, DECLINED, CANCELED, IN_PROGRESS, DONE, Parameters.USER_ID);

    private static final String CONDITION_TO_GET_TICKETS_OF_ENGINEER = format(
        "(t.owner.role in(''{0}'', ''{1}'')"
            + " and t.state = ''{2}''"
            + " or t.assignee.id = :{5}"
            + " and t.state in(''{3}'', ''{4}''))",
        EMPLOYEE, MANAGER, APPROVED, IN_PROGRESS, DONE, Parameters.USER_ID);

    private static final String CONDITION_TO_GET_PERSONAL_TICKETS_OF_EMPLOYEE = format(
        "(t.owner.id = :{0})",
        Parameters.USER_ID);

    private static final String CONDITION_TO_GET_PERSONAL_TICKETS_OF_MANAGER = format(
        "(t.owner.id = :{0} or t.approver.id = :{0} and t.state = ''{1}'')",
        Parameters.USER_ID, APPROVED);

    private static final String CONDITION_TO_GET_PERSONAL_TICKETS_OF_ENGINEER = format(
        "(t.assignee.id = :{0})",
        Parameters.USER_ID);

    public static final Map<Role, String> MAP_OF_CONDITIONS_TO_GET_TICKETS = Map.of(
        EMPLOYEE, CONDITION_TO_GET_TICKETS_OF_EMPLOYEE,
        MANAGER, CONDITION_TO_GET_TICKETS_OF_MANAGER,
        ENGINEER, CONDITION_TO_GET_TICKETS_OF_ENGINEER);

    public static final Map<Role, String> MAP_OF_CONDITIONS_TO_GET_PERSONAL_TICKETS = Map.of(
        EMPLOYEE, CONDITION_TO_GET_PERSONAL_TICKETS_OF_EMPLOYEE,
        MANAGER, CONDITION_TO_GET_PERSONAL_TICKETS_OF_MANAGER,
        ENGINEER, CONDITION_TO_GET_PERSONAL_TICKETS_OF_ENGINEER);

    private static final String CONDITION_TO_EDIT_TICKET = format(
        "(t.owner.id = :{0})", Parameters.USER_ID)
        + AND + format("(t.state = ''{0}'')", State.DRAFT);

    private static final String CONDITION_TO_SAVE_FEEDBACK = format(
        "(t.state = ''{0}'')", State.DONE)
        + AND + format("(t.owner.id = :{0})", Parameters.USER_ID);
  }

  @NoArgsConstructor(access = PRIVATE)
  public static final class Parameters {

    public static final String USER_ID = "user_id";

    public static final String KEYWORD = "keyword";

    public static final String CATEGORY_ID = "category_id";

    public static final String NAME = "name";

    public static final String DESCRIPTION = "description";

    public static final String URGENCY = "urgency";

    public static final String DESIRED_DATE = "desired_date";

    public static final String TICKET_ID = "ticket_id";
  }
}
