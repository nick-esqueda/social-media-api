package com.nickesqueda.socialmediademo.controller;

import static org.springframework.http.HttpStatus.CREATED;

import com.nickesqueda.socialmediademo.dto.AuthCredentialsDto;
import com.nickesqueda.socialmediademo.dto.LoginResponseDto;
import com.nickesqueda.socialmediademo.dto.RegistrationResponseDto;
import com.nickesqueda.socialmediademo.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

  private final AuthService authService;

  @PostMapping("/register")
  @ResponseStatus(CREATED)
  public RegistrationResponseDto registerUser(
      @RequestBody @Valid AuthCredentialsDto authCredentialsDto) {
    return authService.registerUser(authCredentialsDto);
  }

  @PostMapping("/login")
  public LoginResponseDto passwordLogin(@RequestBody @Valid AuthCredentialsDto authCredentialsDto) {
    return authService.passwordLogin(authCredentialsDto);
  }
}
