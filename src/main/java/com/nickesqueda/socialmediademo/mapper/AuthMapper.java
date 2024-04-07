package com.nickesqueda.socialmediademo.mapper;

import com.nickesqueda.socialmediademo.dto.LoginResponseDto;
import com.nickesqueda.socialmediademo.dto.RegistrationResponseDto;
import com.nickesqueda.socialmediademo.dto.UserCredentialsDto;
import com.nickesqueda.socialmediademo.entity.Role;
import com.nickesqueda.socialmediademo.entity.UserEntity;
import java.util.Collection;
import java.util.Collections;

public class AuthMapper {
  public static UserEntity toEntity(
      UserCredentialsDto userCredentialsDto, String passwordHash, Role role) {
    UserEntity userEntity = new UserEntity();
    userEntity.setUsername(userCredentialsDto.getUsername());
    userEntity.setPasswordHash(passwordHash);
    userEntity.setRoles(Collections.singletonList(role));
    // don't set createdAt/updatedAt. allow Spring Data JPA to set these automatically.
    return userEntity;
  }

  public static LoginResponseDto toDto(UserEntity userEntity, String authToken) {
    LoginResponseDto loginResponseDto = new LoginResponseDto();
    loginResponseDto.setId(userEntity.getId());
    loginResponseDto.setUsername(userEntity.getUsername());
    loginResponseDto.setFirstName(userEntity.getFirstName());
    loginResponseDto.setLastName(userEntity.getLastName());
    loginResponseDto.setEmail(userEntity.getEmail());
    loginResponseDto.setPhoneNumber(userEntity.getPhoneNumber());
    loginResponseDto.setGender(userEntity.getGender());
    loginResponseDto.setBio(userEntity.getBio());
    loginResponseDto.setCreatedAt(userEntity.getCreatedAt());
    loginResponseDto.setUpdatedAt(userEntity.getUpdatedAt());
    loginResponseDto.setAuthToken(authToken);
    return loginResponseDto;
  }

  public static RegistrationResponseDto toDto(
      LoginResponseDto loginResponseDto, Collection<Role> roles) {
    RegistrationResponseDto registrationResponseDto = new RegistrationResponseDto();
    registrationResponseDto.setId(loginResponseDto.getId());
    registrationResponseDto.setUsername(loginResponseDto.getUsername());
    registrationResponseDto.setCreatedAt(loginResponseDto.getCreatedAt());
    registrationResponseDto.setUpdatedAt(loginResponseDto.getUpdatedAt());
    registrationResponseDto.setRoles(roles);
    registrationResponseDto.setAuthToken(loginResponseDto.getAuthToken());
    return registrationResponseDto;
  }
}
