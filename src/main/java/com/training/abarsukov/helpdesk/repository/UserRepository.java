package com.training.abarsukov.helpdesk.repository;

import com.training.abarsukov.helpdesk.model.User;
import com.training.abarsukov.helpdesk.model.enums.Role;
import com.training.abarsukov.helpdesk.repository.generic.Repository;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends Repository<User, Long> {

  Optional<User> findByEmail(String email);

  List<User> findByRole(Role role);
}
