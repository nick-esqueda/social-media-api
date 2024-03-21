package com.nickesqueda.socialmediademo.controller;

import com.nickesqueda.socialmediademo.dto.UserCredentialsDto;
import com.nickesqueda.socialmediademo.dto.UserDto;
import com.nickesqueda.socialmediademo.exception.UsernameUnavailableException;
import com.nickesqueda.socialmediademo.repository.UserRepository;
import com.nickesqueda.socialmediademo.service.AuthService;
import javax.management.relation.RoleNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

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
  @ResponseStatus(CREATED)
  public UserDto register(@RequestBody UserCredentialsDto userCredentialsDto) {
    String username = userCredentialsDto.getUsername();
    if (userRepository.existsByUsername(username)) {
      throw new UsernameUnavailableException(username);
    }
    // TODO: return 201 CREATED with new resource location using UriComponentsBuilder
    // URI location = ServletUriComponentsBuilder.
    return authService.registerNewUser(userCredentialsDto);
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody UserCredentialsDto userCredentialsDto) {
    String jwt = authService.authenticateUser(userCredentialsDto);
    // TODO: return JSON instead of plain text
    return ResponseEntity.ok(jwt);
  }
}
