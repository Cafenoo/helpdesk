package com.training.abarsukov.helpdesk.model;

import com.training.abarsukov.helpdesk.model.enums.State;
import com.training.abarsukov.helpdesk.model.enums.Urgency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ticket implements Serializable {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  @Column(nullable = false, length = 100)
  private String name;

  @Column(length = 500)
  private String description;

  @Column(updatable = false)
  private Date createdOn;

  private Date desiredResolutionDate;

  @ToString.Exclude
  @ManyToOne(fetch = LAZY)
  private User assignee;

  @ToString.Exclude
  @ManyToOne(fetch = LAZY)
  @JoinColumn(nullable = false, updatable = false)
  private User owner;

  @Enumerated(STRING)
  @Column(nullable = false)
  private State state;

  @ToString.Exclude
  @ManyToOne(fetch = LAZY)
  @JoinColumn(nullable = false)
  private Category category;

  @Enumerated(STRING)
  @Column(nullable = false)
  private Urgency urgency;

  @ToString.Exclude
  @ManyToOne(fetch = LAZY)
  private User approver;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Ticket ticket = (Ticket) o;
    return Objects.equals(getName(), ticket.getName())
        && Objects.equals(getDescription(), ticket.getDescription())
        && Objects.equals(getCreatedOn(), ticket.getCreatedOn())
        && Objects.equals(getDesiredResolutionDate(), ticket.getDesiredResolutionDate())
        && getState() == ticket.getState()
        && getUrgency() == ticket.getUrgency();
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        getName(),
        getDescription(),
        getCreatedOn(),
        getDesiredResolutionDate(),
        getState(),
        getUrgency());
  }
}
