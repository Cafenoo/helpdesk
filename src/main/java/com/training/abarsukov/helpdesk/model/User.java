package com.training.abarsukov.helpdesk.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.training.abarsukov.helpdesk.model.enums.Role;
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
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

  @Id
  @GeneratedValue(strategy = IDENTITY)
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
  @ToString.Exclude
  @Column(nullable = false)
  private String password;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    User user = (User) o;
    return Objects.equals(getFirstName(), user.getFirstName())
        && Objects.equals(getLastName(), user.getLastName())
        && getRole() == user.getRole()
        && Objects.equals(getEmail(), user.getEmail())
        && Objects.equals(getPassword(), user.getPassword());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getFirstName(), getLastName(), getRole(), getEmail(), getPassword());
  }
}
