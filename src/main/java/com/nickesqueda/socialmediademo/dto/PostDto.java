package com.nickesqueda.socialmediademo.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class PostDto {
  private Long id;
  private String content;
  private Instant createdAt;
  private Instant updatedAt;
}
