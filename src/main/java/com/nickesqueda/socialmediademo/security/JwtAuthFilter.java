package com.nickesqueda.socialmediademo.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;

import static com.nickesqueda.socialmediademo.security.SecurityConstants.*;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);
    if (authorizationHeader != null && authorizationHeader.startsWith(BEARER)) {
      String jwt = authorizationHeader.substring(BEARER.length());

      // JWT VALIDATION OCCURS HERE WHEN EXTRACTING CLAIMS.
      String username = JwtUtils.extractUsername(jwt);
      Collection<GrantedAuthority> roles = JwtUtils.extractGrantedAuthorities(jwt);
      UsernamePasswordAuthenticationToken authenticationToken =
          new UsernamePasswordAuthenticationToken(username, null, roles);
      // set the current request object as details for the SecurityContext.
      authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
      // populate SecurityContext with the currently authenticated user.
      AuthUtils.populateSpringSecurityContext(authenticationToken);
    }

    filterChain.doFilter(request, response);
  }
}
