package com.nickesqueda.socialmediademo.dto;

import static com.nickesqueda.socialmediademo.util.ValidationConstants.VALIDATION_PATTERN_USERNAME;
import static com.nickesqueda.socialmediademo.util.ValidationConstants.VALIDATION_PATTERN_MESSAGE_USERNAME;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserCredentialsDto {
  @NotNull
  @Size(min = 2, max = 32)
  @Pattern(regexp = VALIDATION_PATTERN_USERNAME, message = VALIDATION_PATTERN_MESSAGE_USERNAME)
  private String username;

  @NotNull
  @Size(min = 8, max = 64)
  private String password;
}
