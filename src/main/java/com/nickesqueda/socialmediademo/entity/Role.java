package com.nickesqueda.socialmediademo.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Map;

/**
 * TODO: convert to enum
 * TODO: how to implement GrantedAuthority if it is an enum? can you still return a string?
 * TODO: set roleName to not writeable? (only use the pre-defined roles from table)
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(nullable = false, unique = true)
  private String roleName;

  public Role(Map<String, String> mapRepresentationOfRoleObj) {
    this.roleName = mapRepresentationOfRoleObj.get("roleName");
  }

  @Override
  public String getAuthority() {
    return this.roleName;
  }
}
