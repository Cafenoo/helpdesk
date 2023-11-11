package com.training.abarsukov.helpdesk.model;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.training.abarsukov.helpdesk.model.enums.Role;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
public class User implements Serializable {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  private Long id;

  private String firstName;

  private String lastName;

  @Enumerated(STRING)
  @Column(nullable = false)
  private Role role;

  @JsonIgnore
  @Column(nullable = false, unique = true)
  private String email;

  @JsonIgnore
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  @Column(nullable = false)
  private String password;
}
