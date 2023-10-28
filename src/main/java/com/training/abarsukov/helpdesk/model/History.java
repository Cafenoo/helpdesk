package com.training.abarsukov.helpdesk.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

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
public class History implements Serializable {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  @ToString.Exclude
  @ManyToOne(fetch = LAZY)
  @JoinColumn(nullable = false, updatable = false)
  private Ticket ticket;

  @JoinColumn(nullable = false, updatable = false)
  private Timestamp date;

  @Column(nullable = false, updatable = false)
  private String action;

  @ToString.Exclude
  @ManyToOne
  @JoinColumn(nullable = false, updatable = false)
  private User user;

  @Column(nullable = false, updatable = false)
  private String description;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    History history = (History) o;
    return Objects.equals(getDate(), history.getDate())
        && Objects.equals(getAction(), history.getAction())
        && Objects.equals(getDescription(), history.getDescription());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getDate(), getAction(), getDescription());
  }
}
