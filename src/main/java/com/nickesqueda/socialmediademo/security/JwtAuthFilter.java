package com.nickesqueda.socialmediademo.security;

import static com.nickesqueda.socialmediademo.security.SecurityConstants.*;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);
    if (authorizationHeader != null && authorizationHeader.startsWith(BEARER)) {
      String authToken = authorizationHeader.substring(BEARER.length());

      // JWT VALIDATION OCCURS HERE WHEN EXTRACTING CLAIMS.
      Long userId = JwtUtils.extractUserId(authToken);
      Collection<GrantedAuthority> roles = JwtUtils.extractGrantedAuthorities(authToken);
      UsernamePasswordAuthenticationToken authentication =
          new UsernamePasswordAuthenticationToken(userId, null, roles);

      AuthUtils.setSecurityContext(authentication);
    }

    filterChain.doFilter(request, response);
  }
}
