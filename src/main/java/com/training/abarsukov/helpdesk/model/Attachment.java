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
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Arrays;
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
public class Attachment implements Serializable {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  @Lob
  @Column(nullable = false)
  private byte[] blob;

  @ToString.Exclude
  @OneToOne(fetch = LAZY)
  @JoinColumn(unique = true, nullable = false)
  private Ticket ticket;

  @Column(nullable = false)
  private String name;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Attachment that = (Attachment) o;
    return Arrays.equals(getBlob(), that.getBlob()) && Objects.equals(getName(), that.getName());
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(getName());
    result = 31 * result + Arrays.hashCode(getBlob());
    return result;
  }
}
