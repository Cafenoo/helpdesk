package com.training.abarsukov.helpdesk.repository.impl;

import com.training.abarsukov.helpdesk.model.User;
import com.training.abarsukov.helpdesk.model.enums.Role;
import com.training.abarsukov.helpdesk.repository.UserRepository;
import com.training.abarsukov.helpdesk.repository.generic.abstracts.AbstractRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserRepositoryV1 extends AbstractRepository<User, Long> implements UserRepository {

  private static final String SELECT_USER = "from User u";

  private static final String BY_EMAIL = " where u.email = :email";

  private static final String BY_ROLE = " where u.role = :role";

  @Override
  public Optional<User> findByEmail(String email) {
    final Map<String, Object> parameters = Map.of("email", email);
    return findByParameters(SELECT_USER + BY_EMAIL, parameters).stream().findFirst();
  }

  @Override
  public List<User> findByRole(Role role) {
    final Map<String, Object> parameters = Map.of("role", role);
    return findByParameters(SELECT_USER + BY_ROLE, parameters);
  }
}
