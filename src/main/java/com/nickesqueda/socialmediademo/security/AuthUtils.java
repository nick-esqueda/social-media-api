package com.nickesqueda.socialmediademo.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthUtils {

  public Long getCurrentAuthenticatedUserId() {
    return (Long) getCurrentAuthentication().getPrincipal();
  }

  public Authentication getCurrentAuthentication() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      // TODO: throw a custom exception.
      throw new RuntimeException("User is not authenticated");
    }
    return authentication;
  }

  /**
   * @param authentication Sets the authenticated user in the SecurityContext. Spring Security will
   *     use this as the currently authenticated user. The authenticated user can be accessed
   *     anywhere by getting it from the SecurityContext.
   */
  public void setSecurityContext(Authentication authentication) {
    SecurityContext context = SecurityContextHolder.createEmptyContext();
    context.setAuthentication(authentication);
    SecurityContextHolder.setContext(context);
  }
}
