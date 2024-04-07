package com.nickesqueda.socialmediademo.service;

import com.nickesqueda.socialmediademo.dto.LoginResponseDto;
import com.nickesqueda.socialmediademo.dto.UserDto;
import com.nickesqueda.socialmediademo.entity.UserEntity;
import com.nickesqueda.socialmediademo.exception.UnauthorizedOperationException;
import com.nickesqueda.socialmediademo.mapper.UserMapper;
import com.nickesqueda.socialmediademo.repository.UserRepository;
import com.nickesqueda.socialmediademo.security.AuthUtils;
import org.springframework.stereotype.Service;

@Service
public class UserService {
  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public UserDto getUser(Long userId) {
    UserEntity userEntity = userRepository.retrieveOrElseThrow(userId);
    return UserMapper.toDto(userEntity);
  }

  public UserDto updateUser(Long userId, LoginResponseDto updatedUser) {
    Long currentUserId = AuthUtils.getCurrentAuthenticatedUserId();

    if (!currentUserId.equals(userId)) {
      throw new UnauthorizedOperationException();
    }

    UserEntity userEntity = userRepository.retrieveOrElseThrow(userId);
    userEntity.setUsername(updatedUser.getUsername());
    userEntity.setFirstName(updatedUser.getFirstName());
    userEntity.setLastName(updatedUser.getLastName());
    userEntity.setEmail(updatedUser.getEmail());
    userEntity.setPhoneNumber(updatedUser.getPhoneNumber());
    userEntity.setBirthday(updatedUser.getBirthday());
    userEntity.setGender(updatedUser.getGender());
    userEntity.setBio(updatedUser.getBio());
    userEntity = userRepository.save(userEntity);

    return UserMapper.toDto(userEntity);
  }
}
