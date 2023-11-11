package com.training.abarsukov.helpdesk.security;

import com.training.abarsukov.helpdesk.model.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface ApplicationUserDetails extends UserDetails {

  User getUser();
}
