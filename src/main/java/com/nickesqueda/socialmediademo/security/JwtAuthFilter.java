package com.nickesqueda.socialmediademo.security;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.impl.crypto.DefaultJwtSignatureValidator;
import io.jsonwebtoken.impl.crypto.JwtSignatureValidator;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String authorizationHeader = request.getHeader("Authorization");
    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
      String jwt = authorizationHeader.substring(7);

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
