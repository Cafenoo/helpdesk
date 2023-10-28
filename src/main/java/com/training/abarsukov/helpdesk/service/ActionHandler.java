package com.training.abarsukov.helpdesk.service;

import com.training.abarsukov.helpdesk.model.Ticket;
import com.training.abarsukov.helpdesk.model.enums.Action;
import com.training.abarsukov.helpdesk.model.enums.State;

import java.util.List;

public interface ActionHandler {
  List<Action> getPossibleActions(State state);

  void transitState(Ticket ticket, Action action);
}
