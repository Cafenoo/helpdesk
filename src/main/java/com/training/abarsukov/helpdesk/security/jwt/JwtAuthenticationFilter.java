package com.training.abarsukov.helpdesk.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.training.abarsukov.helpdesk.dto.UserDto;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.json.JsonParseException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

import static java.lang.System.currentTimeMillis;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final JwtProperties jwtProperties;

  private final AuthenticationManager authenticationManager;

  @Override
  public Authentication attemptAuthentication(
      HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
    final UserDto userDto = getUserCredentialsFromRequest(request);
    final Authentication authentication = createAuthentication(userDto);
    return authenticationManager.authenticate(authentication);
  }

  @Override
  protected void successfulAuthentication(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain,
      Authentication authResult)
      throws IOException {
    final String token = generateToken(authResult);
    final String bearerToken = JwtProperties.TOKEN_PREFIX + token;
    response.addHeader(JwtProperties.TOKEN_HEADER, bearerToken);
    response.getWriter().print(bearerToken);
  }

  private UsernamePasswordAuthenticationToken createAuthentication(UserDto userDto) {
    return new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword());
  }

  private UserDto getUserCredentialsFromRequest(HttpServletRequest request) {
    try {
      return new ObjectMapper().readValue(request.getInputStream(), UserDto.class);
    } catch (IOException exception) {
      throw new JsonParseException(exception);
    }
  }

  private String generateToken(Authentication authResult) {
    return Jwts.builder()
        .setSubject(authResult.getName())
        .claim(JwtProperties.AUTHORITIES_CLAIM, authResult.getAuthorities())
        .setIssuedAt(new Date(currentTimeMillis()))
        .setExpiration(
            new Date(currentTimeMillis() + jwtProperties.getExpirationTimeMinutes() * 60000))
        .signWith(Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes()))
        .compact();
  }
}
