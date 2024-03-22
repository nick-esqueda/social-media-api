package com.nickesqueda.socialmediademo.controller;

import static org.springframework.http.HttpStatus.CREATED;

import com.nickesqueda.socialmediademo.dto.UserCredentialsDto;
import com.nickesqueda.socialmediademo.dto.UserDto;
import com.nickesqueda.socialmediademo.exception.UsernameNotAvailableException;
import com.nickesqueda.socialmediademo.repository.UserRepository;
import com.nickesqueda.socialmediademo.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
  private final AuthService authService;
  private final UserRepository userRepository;

  public AuthController(AuthService authService, UserRepository userRepository) {
    this.authService = authService;
    this.userRepository = userRepository;
  }

  @PostMapping("/register")
  @ResponseStatus(CREATED)
  public UserDto registerUser(@RequestBody UserCredentialsDto userCredentialsDto) {
    String username = userCredentialsDto.getUsername();
    if (userRepository.existsByUsername(username)) {
      throw new UsernameNotAvailableException(username);
    }
    return authService.registerUser(userCredentialsDto);
  }

  @PostMapping("/login")
  public UserDto passwordLogin(@RequestBody UserCredentialsDto userCredentialsDto) {
    return authService.passwordLogin(userCredentialsDto);
  }
}
