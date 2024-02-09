package com.nickesqueda.socialmediademo.controller;

import com.nickesqueda.socialmediademo.dto.UserCredentials;
import com.nickesqueda.socialmediademo.repository.UserRepository;
import com.nickesqueda.socialmediademo.service.AuthService;
import javax.management.relation.RoleNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
  private final AuthService authService;
  private final UserRepository userRepository;

  public AuthController(
      AuthService authService,
      UserRepository userRepository) {
    this.authService = authService;
    this.userRepository = userRepository;
  }

  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody UserCredentials userCredentials)
      throws RoleNotFoundException {
    if (userRepository.existsByUsername(userCredentials.getUsername())) {
      // TODO: use ControllerAdvice exception handling to translate UsernameTaken exception to 400
      // BAD REQUEST.
      return ResponseEntity.badRequest().body("Username already taken.");
    }

    String jwt = authService.registerNewUser(userCredentials);

    // TODO: return 201 CREATED with new resource location using UriComponentsBuilder
    // URI location = ServletUriComponentsBuilder.
    return ResponseEntity.ok(jwt);
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody UserCredentials userCredentials) {
    String jwt = authService.authenticateUser(userCredentials);
    return ResponseEntity.ok(jwt);
  }
}
