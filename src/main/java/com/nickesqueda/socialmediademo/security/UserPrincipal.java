package com.nickesqueda.socialmediademo.security;

import com.nickesqueda.socialmediademo.entity.UserEntity;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public record UserPrincipal(UserEntity userEntity) implements UserDetails {
  public UserEntity getUserEntity() {
    return userEntity;
  }

  public Long getId() {
    return userEntity.getId();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return userEntity.getRoles();
  }

  @Override
  public String getPassword() {
    // TODO: does this need to be the plaintext password?
    return userEntity.getPasswordHash();
  }

  @Override
  public String getUsername() {
    return userEntity.getUsername();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
