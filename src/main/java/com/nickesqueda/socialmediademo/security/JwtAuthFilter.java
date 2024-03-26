package com.nickesqueda.socialmediademo.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
public class JwtAuthFilter extends OncePerRequestFilter {
  public static String AUTHORIZATION_HEADER = "Authorization";
  public static String BEARER = "Bearer "; // trailing space " " is required.

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);
    log.debug("Authorization Header in request: {}", authorizationHeader);

    if (!request.getMethod().equalsIgnoreCase("GET")
        && authorizationHeader != null
        && authorizationHeader.startsWith(BEARER)) {
      String authToken = authorizationHeader.substring(BEARER.length());

      // JWT VALIDATION OCCURS HERE WHEN EXTRACTING CLAIMS.
      Long userId = JwtUtils.extractUserId(authToken);
      Collection<GrantedAuthority> roles = JwtUtils.extractGrantedAuthorities(authToken);
      UsernamePasswordAuthenticationToken authentication =
          new UsernamePasswordAuthenticationToken(userId, null, roles);

      AuthUtils.setSecurityContext(authentication);
      log.info("Authentication for request successful. User ID: {}", userId);
    }

    filterChain.doFilter(request, response);
  }
}
