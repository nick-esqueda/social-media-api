package com.nickesqueda.socialmediademo.dto;

import static com.nickesqueda.socialmediademo.util.ValidationConstants.*;
import static com.nickesqueda.socialmediademo.util.ValidationConstants.BIO_MAX_LENGTH;

import com.nickesqueda.socialmediademo.model.Gender;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {
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
}
