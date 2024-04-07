package com.nickesqueda.socialmediademo.mapper;

import com.nickesqueda.socialmediademo.dto.UserDto;
import com.nickesqueda.socialmediademo.entity.UserEntity;

public class UserMapper {
  public static UserDto toDto(UserEntity userEntity) {
    UserDto userDto = new UserDto();
    userDto.setId(userEntity.getId());
    userDto.setUsername(userEntity.getUsername());
    userDto.setFirstName(userEntity.getFirstName());
    userDto.setLastName(userEntity.getLastName());
    userDto.setEmail(userEntity.getEmail());
    userDto.setPhoneNumber(userEntity.getPhoneNumber());
    userDto.setGender(userEntity.getGender());
    userDto.setBio(userEntity.getBio());
    userDto.setCreatedAt(userEntity.getCreatedAt());
    userDto.setUpdatedAt(userEntity.getUpdatedAt());
    return userDto;
  }
}
