package com.nickesqueda.socialmediademo.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import lombok.Data;

@Data
public class CommentDto {
  private Long id;

  @NotNull
  @Size(min = 1, max = 255)
  private String content;

  @PastOrPresent private Instant createdAt;

  @PastOrPresent private Instant updatedAt;
}
