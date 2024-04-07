package com.nickesqueda.socialmediademo.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PostRequestDto {
  @NotNull
  @Size(min = 1, max = 255)
  private String content;
}
