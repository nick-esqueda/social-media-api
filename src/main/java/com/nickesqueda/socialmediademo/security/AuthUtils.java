package com.nickesqueda.socialmediademo.security;

import com.nickesqueda.socialmediademo.entity.UserEntity;
import com.nickesqueda.socialmediademo.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuthUtils {
  /**
   * @param authentication Sets the authenticated user in the SecurityContext. Spring Security will
   *     use this as the currently authenticated user. The authenticated user can be accessed
   *     anywhere by getting it from the SecurityContext.
   */
  public static void populateSpringSecurityContext(Authentication authentication) {
    SecurityContext context = SecurityContextHolder.createEmptyContext();
    context.setAuthentication(authentication);
    SecurityContextHolder.setContext(context);
  }

  public static Authentication getCurrentAuthentication() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      throw new RuntimeException("User is not authenticated.");
    }
    return authentication;
  }

  private final UserRepository userRepository;

  public AuthUtils(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public UserEntity getCurrentAuthenticatedUser() {
    String username = getCurrentAuthentication().getName();
    Optional<UserEntity> userOptional = userRepository.findByUsername(username);
    return userOptional.orElseThrow(IllegalStateException::new);
  }
}
