package com.training.abarsukov.helpdesk.model;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment implements Serializable {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  private Long id;

  @Column(length = 500, nullable = false)
  private String text;

  @Column(updatable = false, nullable = false)
  private Timestamp date;

  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  @ManyToOne(fetch = LAZY)
  @JoinColumn(nullable = false, updatable = false)
  private User user;

  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  @ManyToOne(fetch = LAZY)
  @JoinColumn(nullable = false, updatable = false)
  private Ticket ticket;
}
