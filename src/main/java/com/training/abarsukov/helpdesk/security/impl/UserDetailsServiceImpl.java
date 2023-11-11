package com.training.abarsukov.helpdesk.security.impl;

import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

import com.training.abarsukov.helpdesk.repository.UserRepository;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  @Transactional(propagation = SUPPORTS, readOnly = true)
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    return userRepository
        .findByEmail(email)
        .map(UserDetailsImpl::new)
        .orElseThrow(
            () -> new EntityNotFoundException("User with email " + email + " was not found"));
  }
}
