package com.training.abarsukov.helpdesk.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Feedback implements Serializable {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  @ToString.Exclude
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(updatable = false, nullable = false)
  private User user;

  @Column(nullable = false, updatable = false)
  private Integer rate;

  @Column(nullable = false, updatable = false)
  private Date date;

  @Column(updatable = false)
  private String text;

  @ToString.Exclude
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(unique = true, nullable = false, updatable = false)
  private Ticket ticket;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Feedback feedback = (Feedback) o;
    return Objects.equals(getUser(), feedback.getUser())
        && Objects.equals(getRate(), feedback.getRate())
        && Objects.equals(getDate(), feedback.getDate())
        && Objects.equals(getText(), feedback.getText());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getUser(), getRate(), getDate(), getText());
  }
}
