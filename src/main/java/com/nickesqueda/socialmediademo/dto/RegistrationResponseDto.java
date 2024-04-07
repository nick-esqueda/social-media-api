package com.nickesqueda.socialmediademo.dto;

import static com.nickesqueda.socialmediademo.util.ValidationConstants.*;

import com.nickesqueda.socialmediademo.entity.Role;
import jakarta.validation.constraints.*;
import java.time.Instant;
import java.util.Collection;
import lombok.Data;

@Data
public class RegistrationResponseDto {
  private Long id;

  @NotNull
  @Size(min = USERNAME_MIN_LENGTH, max = USERNAME_MAX_LENGTH)
  @Pattern(regexp = USERNAME_PATTERN, message = USERNAME_PATTERN_MESSAGE)
  private String username;

  @PastOrPresent private Instant createdAt;

  @PastOrPresent private Instant updatedAt;

  private Collection<Role> roles;

  private String authToken;
}
