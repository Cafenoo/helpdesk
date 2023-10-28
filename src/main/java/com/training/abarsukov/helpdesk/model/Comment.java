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
public class Comment implements Serializable {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  @ToString.Exclude
  @ManyToOne(fetch = LAZY)
  @JoinColumn(nullable = false, updatable = false)
  private User user;

  @Column(length = 500, nullable = false)
  private String text;

  @Column(updatable = false, nullable = false)
  private Timestamp date;

  @ToString.Exclude
  @ManyToOne(fetch = LAZY)
  @JoinColumn(nullable = false, updatable = false)
  private Ticket ticket;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Comment comment = (Comment) o;
    return Objects.equals(getText(), comment.getText())
        && Objects.equals(getDate(), comment.getDate());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getText(), getDate());
  }
}
