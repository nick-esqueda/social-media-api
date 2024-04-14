package com.nickesqueda.socialmediademo.dto;

import static com.nickesqueda.socialmediademo.util.ValidationConstants.*;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {
  private Long id;

  @NotNull
  @Size(min = POST_MIN_LENGTH, max = POST_MAX_LENGTH)
  private String content;

  @PastOrPresent private Instant createdAt;

  @PastOrPresent private Instant updatedAt;
}
