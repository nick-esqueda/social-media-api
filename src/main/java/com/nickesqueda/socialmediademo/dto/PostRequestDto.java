package com.nickesqueda.socialmediademo.dto;

import static com.nickesqueda.socialmediademo.util.ValidationConstants.*;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PostRequestDto {
  @NotNull
  @Size(min = POST_MIN_LENGTH, max = POST_MAX_LENGTH)
  private String content;
}
