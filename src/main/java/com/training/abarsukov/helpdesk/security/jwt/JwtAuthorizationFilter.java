package com.training.abarsukov.helpdesk.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

  private static final String EMPTY_STRING = "";

  private final JwtProperties jwtProperties;

  private final UserDetailsService userDetailsService;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    final String authorizationHeader = request.getHeader(JwtProperties.TOKEN_HEADER);

    if (!isTokenInvalid(authorizationHeader)) {
      final String token = authorizationHeader.replace(JwtProperties.TOKEN_PREFIX, EMPTY_STRING);
      final Claims tokenClaims = validateAndReturnTokenClaims(token);
      setAuthenticationToContext(tokenClaims);
    }

    filterChain.doFilter(request, response);
  }

  private void setAuthenticationToContext(Claims tokenClaims) {
    final String email = tokenClaims.getSubject();

    final UserDetails userDetails = userDetailsService.loadUserByUsername(email);

    final Set<? extends GrantedAuthority> grantedAuthorities = getGrantedAuthorities(tokenClaims);

    final Authentication authentication =
        new UsernamePasswordAuthenticationToken(userDetails, null, grantedAuthorities);

    SecurityContextHolder.getContext().setAuthentication(authentication);
  }

  @SuppressWarnings("unchecked")
  private Set<? extends GrantedAuthority> getGrantedAuthorities(Claims tokenClaims) {
    final List<Map<String, String>> authorities =
        (List<Map<String, String>>) tokenClaims.get(JwtProperties.AUTHORITIES_CLAIM);

    return authorities.stream()
        .map(m -> new SimpleGrantedAuthority(m.get(JwtProperties.AUTHORITY_CLAIM)))
        .collect(Collectors.toSet());
  }

  private Claims validateAndReturnTokenClaims(String token) {
    try {
      final Jws<Claims> claimsJws =
          Jwts.parser()
              .setSigningKey(Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes()))
              .parseClaimsJws(token);

      return claimsJws.getBody();
    } catch (JwtException e) {
      throw new JwtException("Token cannot be trusted");
    }
  }

  private boolean isTokenInvalid(String header) {
    return header == null || !header.startsWith(JwtProperties.TOKEN_PREFIX);
  }
}
