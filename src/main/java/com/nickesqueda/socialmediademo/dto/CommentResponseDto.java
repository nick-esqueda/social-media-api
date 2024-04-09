package com.nickesqueda.socialmediademo.dto;

import static com.nickesqueda.socialmediademo.util.ValidationConstants.*;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import lombok.Data;

@Data
public class CommentResponseDto {
  private Long id;

  @NotNull
  @Size(min = COMMENT_MIN_LENGTH, max = COMMENT_MAX_LENGTH)
  private String content;

  @PastOrPresent private Instant createdAt;

  @PastOrPresent private Instant updatedAt;
}
