package com.training.abarsukov.helpdesk.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class EncoderConfiguration {

  public static final int PASSWORD_ENCODER_STRENGTH = 4;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(PASSWORD_ENCODER_STRENGTH);
  }
}
