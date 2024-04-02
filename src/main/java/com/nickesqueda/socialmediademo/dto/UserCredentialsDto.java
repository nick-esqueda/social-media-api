package com.nickesqueda.socialmediademo.dto;

import static com.nickesqueda.socialmediademo.util.ValidationConstants.*;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserCredentialsDto {
  @NotNull
  @Size(min = USERNAME_MIN_LENGTH, max = USERNAME_MAX_LENGTH)
  @Pattern(regexp = USERNAME_PATTERN, message = USERNAME_PATTERN_MESSAGE)
  private String username;

  @NotNull
  @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
  private String password;
}
