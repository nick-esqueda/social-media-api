package com.nickesqueda.socialmediademo.service;

import com.nickesqueda.socialmediademo.dto.UserRequestDto;
import com.nickesqueda.socialmediademo.dto.UserResponseDto;
import com.nickesqueda.socialmediademo.entity.UserEntity;
import com.nickesqueda.socialmediademo.exception.UnauthorizedOperationException;
import com.nickesqueda.socialmediademo.repository.UserRepository;
import com.nickesqueda.socialmediademo.security.AuthUtils;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@RequiredArgsConstructor
@Validated
@Service
public class UserService {

  private final UserRepository userRepository;
  private final AuthUtils authUtils;
  private final ModelMapper modelMapper;

  public UserResponseDto getUser(@NotNull Long userId) {
    UserEntity userEntity = userRepository.retrieveOrElseThrow(userId);
    return modelMapper.map(userEntity, UserResponseDto.class);
  }

  public UserResponseDto updateUser(@NotNull Long userId, @NotNull UserRequestDto updatedUser) {
    Long currentUserId = authUtils.getCurrentAuthenticatedUserId();

    if (!currentUserId.equals(userId)) {
      throw new UnauthorizedOperationException();
    }

    UserEntity userEntity = userRepository.retrieveOrElseThrow(userId);
    modelMapper.map(updatedUser, userEntity);
    userEntity = userRepository.save(userEntity);

    return modelMapper.map(userEntity, UserResponseDto.class);
  }
}
