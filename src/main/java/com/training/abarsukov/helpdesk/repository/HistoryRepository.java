package com.training.abarsukov.helpdesk.repository;

import com.training.abarsukov.helpdesk.model.History;
import com.training.abarsukov.helpdesk.model.User;
import com.training.abarsukov.helpdesk.repository.generic.Repository;
import java.util.List;

public interface HistoryRepository extends Repository<History, Long> {

  List<History> findByTicketId(Long ticketId, boolean doGetAll, User user);
}
