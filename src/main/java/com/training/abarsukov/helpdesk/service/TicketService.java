package com.training.abarsukov.helpdesk.service;

import com.training.abarsukov.helpdesk.dto.TicketDto;
import com.training.abarsukov.helpdesk.model.Ticket;
import com.training.abarsukov.helpdesk.model.enums.Action;
import com.training.abarsukov.helpdesk.model.enums.SortingField;
import java.util.List;

public interface TicketService {

  List<TicketDto> findAll(
      Integer page,
      Integer pageSize,
      SortingField field,
      String keyword,
      Boolean isPersonal);

  TicketDto findById(Long id);

  Ticket save(TicketDto ticketDto);

  void edit(Long id, TicketDto ticketDto);

  void transitState(Long id, Action action);
}
