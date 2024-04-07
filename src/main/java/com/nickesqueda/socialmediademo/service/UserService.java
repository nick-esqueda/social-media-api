package com.nickesqueda.socialmediademo.service;

import com.nickesqueda.socialmediademo.dto.UserRequestDto;
import com.nickesqueda.socialmediademo.dto.UserResponseDto;
import com.nickesqueda.socialmediademo.entity.UserEntity;
import com.nickesqueda.socialmediademo.exception.UnauthorizedOperationException;
import com.nickesqueda.socialmediademo.repository.UserRepository;
import com.nickesqueda.socialmediademo.security.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

  private final UserRepository userRepository;
  private final ModelMapper modelMapper;

  public UserResponseDto getUser(Long userId) {
    UserEntity userEntity = userRepository.retrieveOrElseThrow(userId);
    return modelMapper.map(userEntity, UserResponseDto.class);
  }

  public UserResponseDto updateUser(Long userId, UserRequestDto updatedUser) {
    Long currentUserId = AuthUtils.getCurrentAuthenticatedUserId();

    if (!currentUserId.equals(userId)) {
      throw new UnauthorizedOperationException();
    }

    UserEntity userEntity = userRepository.retrieveOrElseThrow(userId);
    modelMapper.map(updatedUser, userEntity);
    userEntity = userRepository.save(userEntity);

    return modelMapper.map(userEntity, UserResponseDto.class);
  }
}
