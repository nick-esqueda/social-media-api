package com.nickesqueda.socialmediademo.entity;

import jakarta.persistence.*;
import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

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
  private Long id;

  @Column(nullable = false, unique = true)
  private String roleName; // TODO: is this the same as "authority"? is this field needed?

  // TODO: find alternate way to create Role from JSON
  public Role(Map<String, String> mapRepresentationOfRoleObj) {
    this.roleName = mapRepresentationOfRoleObj.get("roleName");
  }

  public Role(String roleName) {
    this.roleName = roleName;
  }

  @Override
  public String getAuthority() {
    return this.roleName;
  }
}
