package com.nickesqueda.socialmediademo.dto;

import static com.nickesqueda.socialmediademo.util.ValidationConstants.VALIDATION_PATTERN_USERNAME;
import static com.nickesqueda.socialmediademo.util.ValidationConstants.VALIDATION_PATTERN_MESSAGE_USERNAME;

import com.nickesqueda.socialmediademo.entity.Role;
import jakarta.validation.constraints.*;
import java.time.Instant;
import java.util.Collection;
import lombok.Data;

@Data
public class UserDto {
  private Long id;

  @NotNull
  @Size(min = 2, max = 32)
  @Pattern(regexp = VALIDATION_PATTERN_USERNAME, message = VALIDATION_PATTERN_MESSAGE_USERNAME)
  private String username;

  private Collection<Role> roles;

  private String authToken;

  @PastOrPresent private Instant createdAt;

  @PastOrPresent private Instant updatedAt;
}
