package com.training.abarsukov.helpdesk.security;

import com.training.abarsukov.helpdesk.model.User;
import com.training.abarsukov.helpdesk.model.enums.Role;

import java.util.List;

public interface UserService {
  ApplicationUserDetails getPrincipal();

  User getUser();

  List<User> getUsersByRole(Role role);
}
