package com.nickesqueda.socialmediademo.service;

import com.nickesqueda.socialmediademo.dto.UserDto;
import com.nickesqueda.socialmediademo.entity.UserEntity;
import com.nickesqueda.socialmediademo.exception.UnauthorizedOperationException;
import com.nickesqueda.socialmediademo.repository.UserRepository;
import com.nickesqueda.socialmediademo.security.AuthUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class UserService {
  private final UserRepository userRepository;
  private final ModelMapper modelMapper;

  public UserService(UserRepository userRepository, ModelMapper modelMapper) {
    this.userRepository = userRepository;
    this.modelMapper = modelMapper;
  }

  public UserDto getUser(Long userId) {
    UserEntity userEntity = userRepository.retrieveOrElseThrow(userId);
    return modelMapper.map(userEntity, UserDto.class);
  }

  public UserDto updateUser(Long userId, UserDto updatedUser) {
    Long currentUserId = AuthUtils.getCurrentAuthenticatedUserId();

    if (!currentUserId.equals(userId)) {
      throw new UnauthorizedOperationException();
    }

    UserEntity userEntity = userRepository.retrieveOrElseThrow(userId);
    modelMapper.map(updatedUser, userEntity);
    userEntity = userRepository.save(userEntity);

    return modelMapper.map(userEntity, UserDto.class);
  }
}
