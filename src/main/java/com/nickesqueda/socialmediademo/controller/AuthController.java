package com.nickesqueda.socialmediademo.controller;

import static org.springframework.http.HttpStatus.CREATED;

import com.nickesqueda.socialmediademo.dto.LoginResponseDto;
import com.nickesqueda.socialmediademo.dto.RegistrationResponseDto;
import com.nickesqueda.socialmediademo.dto.UserCredentialsDto;
import com.nickesqueda.socialmediademo.service.AuthService;
import jakarta.validation.Valid;
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
  public RegistrationResponseDto registerUser(
      @RequestBody @Valid UserCredentialsDto userCredentialsDto) {
    return authService.registerUser(userCredentialsDto);
  }

  @PostMapping("/login")
  public LoginResponseDto passwordLogin(@RequestBody @Valid UserCredentialsDto userCredentialsDto) {
    return authService.passwordLogin(userCredentialsDto);
  }
}
