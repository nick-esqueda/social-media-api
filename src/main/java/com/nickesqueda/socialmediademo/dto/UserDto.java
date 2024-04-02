package com.nickesqueda.socialmediademo.dto;

import static com.nickesqueda.socialmediademo.util.ValidationConstants.*;

import com.nickesqueda.socialmediademo.entity.Role;
import com.nickesqueda.socialmediademo.model.Gender;
import jakarta.validation.constraints.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Collection;
import lombok.Data;

@Data
public class UserDto {
  private Long id;

  @NotNull
  @Size(min = USERNAME_MIN_LENGTH, max = USERNAME_MAX_LENGTH)
  @Pattern(regexp = USERNAME_PATTERN, message = USERNAME_PATTERN_MESSAGE)
  private String username;

  @Size(min = FIRST_NAME_MIN_LENGTH, max = FIRST_NAME_MAX_LENGTH)
  private String firstName;

  @Size(min = LAST_NAME_MIN_LENGTH, max = LAST_NAME_MAX_LENGTH)
  private String lastName;

  @Email private String email;

  @Pattern(regexp = PHONE_NUMBER_PATTERN, message = PHONE_NUMBER_PATTERN_MESSAGE)
  private String phoneNumber;

  @Past private LocalDate birthday;

  private Gender gender;

  @Size(min = BIO_MIN_LENGTH, max = BIO_MAX_LENGTH)
  private String bio;

  private Collection<Role> roles;

  private String authToken;

  @PastOrPresent private Instant createdAt;

  @PastOrPresent private Instant updatedAt;
}
