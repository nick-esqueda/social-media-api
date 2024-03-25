package com.nickesqueda.socialmediademo.dto;

import com.nickesqueda.socialmediademo.entity.Role;
import java.time.Instant;
import java.util.Collection;
import lombok.Data;

@Data
public class UserDto {
  private Long id;
  private String username;
  private Collection<Role> roles;
  private String authToken;
  private Instant createdAt;
  private Instant updatedAt;
}
