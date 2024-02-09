package com.nickesqueda.socialmediademo.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthUtils {
  /**
   * @param authentication
   * Sets the authenticated user in the SecurityContext.
   * Spring Security will use this as the currently authenticated user.
   * The authenticated user can be accessed anywhere by getting it from the SecurityContext.
   */
  public static void populateSpringSecurityContext(Authentication authentication) {
    SecurityContext context = SecurityContextHolder.createEmptyContext();
    context.setAuthentication(authentication);
    SecurityContextHolder.setContext(context);
  }
}
