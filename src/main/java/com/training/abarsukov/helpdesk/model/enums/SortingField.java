package com.training.abarsukov.helpdesk.model.enums;

import com.training.abarsukov.helpdesk.model.Ticket;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.util.comparator.Comparators;

import java.util.Comparator;

import static java.util.Comparator.comparing;

@Getter
@RequiredArgsConstructor
public enum SortingField {
  DEFAULT(
      comparing(Ticket::getUrgency)
          .thenComparing(Ticket::getDesiredResolutionDate, Comparators.nullsHigh())),
  ID(comparing(Ticket::getId)),
  ID_DESC(ID.getComparator().reversed()),
  NAME(comparing(Ticket::getName)),
  NAME_DESC(NAME.getComparator().reversed()),
  DESIRED_DATE(comparing(Ticket::getDesiredResolutionDate, Comparators.nullsHigh())),
  DESIRED_DATE_DESC(DESIRED_DATE.getComparator().reversed()),
  URGENCY(comparing(Ticket::getUrgency)),
  URGENCY_DESC(URGENCY.getComparator().reversed()),
  STATE(comparing(ticket -> ticket.getState().name())),
  STATE_DESC(STATE.getComparator().reversed());

  private final Comparator<Ticket> comparator;
}
