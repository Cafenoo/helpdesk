package com.training.abarsukov.helpdesk.security.jwt;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class JwtProperties {

  private static final String SPACE = " ";

  protected static final String TOKEN_PREFIX = "Bearer" + SPACE;

  protected static final String TOKEN_HEADER = "Authorization";

  protected static final String AUTHORITIES_CLAIM = "authorities";

  protected static final String AUTHORITY_CLAIM = "authority";

  @Value("${jwt.properties.secret}")
  private String secret;

  @Value("${jwt.properties.expiration-minutes}")
  private long expirationTimeMinutes;
}
