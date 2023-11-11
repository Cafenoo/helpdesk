package com.training.abarsukov.helpdesk.model;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
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
public class Attachment implements Serializable {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  private Long id;

  @Column(nullable = false)
  private String name;

  @Lob
  @Column(nullable = false)
  private byte[] blob;

  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  @OneToOne(fetch = LAZY)
  @JoinColumn(unique = true, nullable = false)
  private Ticket ticket;
}
