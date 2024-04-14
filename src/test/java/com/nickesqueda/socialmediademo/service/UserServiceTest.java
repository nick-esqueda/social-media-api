package com.nickesqueda.socialmediademo.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.nickesqueda.socialmediademo.dto.UserRequestDto;
import com.nickesqueda.socialmediademo.dto.UserResponseDto;
import com.nickesqueda.socialmediademo.entity.UserEntity;
import com.nickesqueda.socialmediademo.exception.UnauthorizedOperationException;
import com.nickesqueda.socialmediademo.repository.UserRepository;
import com.nickesqueda.socialmediademo.security.AuthUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Import(ValidationAutoConfiguration.class)
@ExtendWith(SpringExtension.class)
class UserServiceTest {

  private static final String TEST_STRING = "TEST";

  @TestConfiguration
  static class UserServiceTestContextConfig {
    @MockBean UserRepository userRepository;
    @MockBean AuthUtils authUtils;

    @Bean
    public UserService userService(ModelMapper modelMapper) {
      return new UserService(userRepository, authUtils, modelMapper);
    }

    @Bean
    public ModelMapper modelMapper() {
      return new ModelMapper();
    }
  }

  @Autowired private UserService userService;
  @Autowired private UserRepository userRepository;
  @Autowired private AuthUtils authUtils;
  private Long testUserId;
  private UserEntity userEntity;
  private UserEntity updatedUserEntity;
  private UserRequestDto userRequestDto;
  private UserRequestDto userRequestDtoUpdate;
  private UserResponseDto userResponseDto;
  private UserResponseDto userResponseDtoUpdate;

  @BeforeEach
  public void setUp() {
    testUserId = 1L;

    userEntity = new UserEntity();
    userEntity.setId(testUserId);
    userEntity.setUsername(TEST_STRING);

    updatedUserEntity = new UserEntity();
    BeanUtils.copyProperties(userEntity, updatedUserEntity);
    updatedUserEntity.setFirstName(TEST_STRING);

    userRequestDto = new UserRequestDto();
    userRequestDto.setUsername(TEST_STRING);

    userRequestDtoUpdate = new UserRequestDto();
    BeanUtils.copyProperties(userRequestDto, userRequestDtoUpdate);
    userRequestDtoUpdate.setFirstName(TEST_STRING);

    userResponseDto = new UserResponseDto();
    userResponseDto.setId(testUserId);
    userResponseDto.setUsername(TEST_STRING);

    userResponseDtoUpdate = new UserResponseDto();
    BeanUtils.copyProperties(updatedUserEntity, userResponseDtoUpdate);
  }

  @Test
  void getUser_ShouldReturnUser_GivenValidId() {
    when(userRepository.retrieveOrElseThrow(anyLong())).thenReturn(userEntity);

    UserResponseDto result = userService.getUser(testUserId);

    assertThat(result).isEqualTo(userResponseDto);
  }

  @Test
  void getUser_ShouldThrow_GivenNullId() {
    testUserId = null;

    assertThatThrownBy(() -> userService.getUser(testUserId))
        .isNotInstanceOf(NullPointerException.class)
        .hasMessageContaining("must not be null");
  }

  @Test
  void updateUser_ShouldReturnUser_GivenValidArgs() {
    when(authUtils.getCurrentAuthenticatedUserId()).thenReturn(testUserId);
    when(userRepository.retrieveOrElseThrow(anyLong())).thenReturn(userEntity);
    when(userRepository.save(any(UserEntity.class))).thenReturn(updatedUserEntity);

    UserResponseDto result = userService.updateUser(testUserId, userRequestDtoUpdate);

    assertThat(result).isEqualTo(userResponseDtoUpdate);
  }

  @Test
  void updateUser_ShouldThrow_GivenNullArgs() {
    assertThatThrownBy(() -> userService.updateUser(null, userRequestDto))
        .isNotInstanceOf(NullPointerException.class)
        .hasMessageContaining("must not be null");

    assertThatThrownBy(() -> userService.updateUser(testUserId, null))
        .isNotInstanceOf(NullPointerException.class)
        .hasMessageContaining("must not be null");
  }

  @Test
  void updateUser_ShouldThrow_GivenUnauthenticatedUserId() {
    when(authUtils.getCurrentAuthenticatedUserId()).thenReturn(testUserId);

    assertThatThrownBy(() -> userService.updateUser(2L, userRequestDto))
        .isInstanceOf(UnauthorizedOperationException.class);
  }
}
