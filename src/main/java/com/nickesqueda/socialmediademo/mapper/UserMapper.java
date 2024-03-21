package com.nickesqueda.socialmediademo.mapper;

import com.nickesqueda.socialmediademo.dto.UserCredentialsDto;
import com.nickesqueda.socialmediademo.dto.UserDto;
import com.nickesqueda.socialmediademo.entity.Role;
import com.nickesqueda.socialmediademo.entity.UserEntity;

import java.util.Collections;

public class UserMapper {
  public static UserEntity toEntity(UserDto userDto) {
    UserEntity userEntity = new UserEntity();
    userEntity.setId(userDto.getId());
    userEntity.setUsername(userDto.getUsername());
    userEntity.setRoles(userDto.getRoles());
    return userEntity;
  }

  public static UserEntity toEntity(
      UserCredentialsDto userCredentialsDto, String passwordHash, Role role) {
    UserEntity userEntity = new UserEntity();
    userEntity.setUsername(userCredentialsDto.getUsername());
    userEntity.setPasswordHash(passwordHash);
    userEntity.setRoles(Collections.singletonList(role));
    return userEntity;
  }

  public static UserDto toDto(UserEntity userEntity) {
    UserDto userDto = new UserDto();
    userDto.setId(userEntity.getId());
    userDto.setUsername(userEntity.getUsername());
    userDto.setRoles(userEntity.getRoles());
    return userDto;
  }

  public static UserDto toDto(UserEntity userEntity, String authToken) {
    UserDto userDto = new UserDto();
    userDto.setId(userEntity.getId());
    userDto.setUsername(userEntity.getUsername());
    userDto.setRoles(userEntity.getRoles());
    userDto.setAuthToken(authToken);
    return userDto;
  }
}
