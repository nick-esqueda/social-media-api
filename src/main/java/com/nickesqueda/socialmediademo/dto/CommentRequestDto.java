package com.nickesqueda.socialmediademo.dto;

import static com.nickesqueda.socialmediademo.util.ValidationConstants.*;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentRequestDto {
  @NotNull
  @Size(min = COMMENT_MIN_LENGTH, max = COMMENT_MAX_LENGTH)
  private String content;
}
