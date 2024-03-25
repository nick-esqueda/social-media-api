package com.nickesqueda.socialmediademo.controller;

import static org.springframework.http.HttpStatus.CREATED;

import com.nickesqueda.socialmediademo.dto.UserCredentialsDto;
import com.nickesqueda.socialmediademo.dto.UserDto;
import com.nickesqueda.socialmediademo.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/register")
  @ResponseStatus(CREATED)
  public UserDto registerUser(@RequestBody UserCredentialsDto userCredentialsDto) {
    return authService.registerUser(userCredentialsDto);
  }

  @PostMapping("/login")
  public UserDto passwordLogin(@RequestBody UserCredentialsDto userCredentialsDto) {
    return authService.passwordLogin(userCredentialsDto);
  }
}
