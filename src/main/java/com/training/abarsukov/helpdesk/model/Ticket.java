package com.training.abarsukov.helpdesk.model;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

import com.training.abarsukov.helpdesk.model.enums.State;
import com.training.abarsukov.helpdesk.model.enums.Urgency;
import java.io.Serializable;
import java.sql.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
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
public class Ticket implements Serializable {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  private Long id;

  @Column(nullable = false, length = 100)
  private String name;

  @Column(length = 500)
  private String description;

  @Column(updatable = false)
  private Date createdOn;

  private Date desiredResolutionDate;

  @Enumerated(STRING)
  @Column(nullable = false)
  private State state;

  @Enumerated(STRING)
  @Column(nullable = false)
  private Urgency urgency;

  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  @ManyToOne(fetch = LAZY)
  private User assignee;

  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  @ManyToOne(fetch = LAZY)
  @JoinColumn(nullable = false, updatable = false)
  private User owner;

  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  @ManyToOne(fetch = LAZY)
  @JoinColumn(nullable = false)
  private Category category;

  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  @ManyToOne(fetch = LAZY)
  private User approver;
}
