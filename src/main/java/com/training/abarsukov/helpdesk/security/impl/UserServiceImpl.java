package com.training.abarsukov.helpdesk.security.impl;

import com.training.abarsukov.helpdesk.model.User;
import com.training.abarsukov.helpdesk.model.enums.Role;
import com.training.abarsukov.helpdesk.repository.UserRepository;
import com.training.abarsukov.helpdesk.security.ApplicationUserDetails;
import com.training.abarsukov.helpdesk.security.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  @Override
  public ApplicationUserDetails getPrincipal() {
    return (ApplicationUserDetails)
        SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }

  @Override
  public User getUser() {
    return this.getPrincipal().getUser();
  }

  @Override
  @Transactional(propagation = SUPPORTS, readOnly = true)
  public List<User> getUsersByRole(Role role) {
    return userRepository.findByRole(role);
  }
}
