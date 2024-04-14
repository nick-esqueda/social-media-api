package com.nickesqueda.socialmediademo.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.nickesqueda.socialmediademo.config.ServiceLayerTestContextConfig;
import com.nickesqueda.socialmediademo.dto.UserRequestDto;
import com.nickesqueda.socialmediademo.dto.UserResponseDto;
import com.nickesqueda.socialmediademo.entity.UserEntity;
import com.nickesqueda.socialmediademo.exception.ResourceNotFoundException;
import com.nickesqueda.socialmediademo.exception.UnauthorizedOperationException;
import com.nickesqueda.socialmediademo.repository.UserRepository;
import com.nickesqueda.socialmediademo.security.AuthUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * NOTE: ServiceLayerTestContextConfig.class is imported to use the @TestConfiguration that will be
 * used by @ExtendWith(SpringExtension.class) to configure a Spring Context.
 * ValidationAutoConfiguration.class is imported to enable @NotNull and @Validated on service
 * classes.
 */
@Import({ServiceLayerTestContextConfig.class, ValidationAutoConfiguration.class})
@ExtendWith(SpringExtension.class)
class UserServiceTest {

  private static final String TEST_STRING = "TEST";

  @Autowired private UserService userService;
  @Autowired private UserRepository userRepository;
  @Autowired private AuthUtils authUtils;
  private Long testUserId;
  private Long unauthorizedUserId;
  private UserEntity userEntity;
  private UserEntity updatedUserEntity;
  private UserRequestDto userRequestDto;
  private UserRequestDto userRequestDtoUpdate;
  private UserResponseDto userResponseDto;
  private UserResponseDto userResponseDtoUpdate;

  @BeforeEach
  public void setUp() {
    testUserId = 1L;
    unauthorizedUserId = 2L;
    userEntity = UserEntity.builder().id(testUserId).username(TEST_STRING).build();
    updatedUserEntity = userEntity.toBuilder().firstName(TEST_STRING).build();
    userRequestDto = UserRequestDto.builder().username(TEST_STRING).build();
    userRequestDtoUpdate = userRequestDto.toBuilder().firstName(TEST_STRING).build();
    userResponseDto = UserResponseDto.builder().id(testUserId).username(TEST_STRING).build();
    userResponseDtoUpdate = new UserResponseDto();
    BeanUtils.copyProperties(updatedUserEntity, userResponseDtoUpdate);
  }

  @Test
  void getUser_ShouldReturnUser_GivenValidId() {
    when(userRepository.retrieveOrElseThrow(testUserId)).thenReturn(userEntity);

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
  void getUser_ShouldThrow_GivenUserDoesNotExist() {
    testUserId = 10000L;
    when(userRepository.retrieveOrElseThrow(testUserId)).thenThrow(ResourceNotFoundException.class);

    assertThatThrownBy(() -> userService.getUser(testUserId))
        .isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  void updateUser_ShouldReturnUser_GivenValidArgs() {
    when(authUtils.getCurrentAuthenticatedUserId()).thenReturn(testUserId);
    when(userRepository.retrieveOrElseThrow(testUserId)).thenReturn(userEntity);
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
  void updateUser_ShouldThrow_GivenUnauthorizedUserId() {
    when(authUtils.getCurrentAuthenticatedUserId()).thenReturn(unauthorizedUserId);

    assertThatThrownBy(() -> userService.updateUser(testUserId, userRequestDto))
        .isInstanceOf(UnauthorizedOperationException.class);
  }

  @Test
  void updateUser_ShouldThrow_GivenUserDoesNotExist() {
    testUserId = 10000L;
    when(authUtils.getCurrentAuthenticatedUserId()).thenReturn(testUserId);
    when(userRepository.retrieveOrElseThrow(testUserId)).thenThrow(ResourceNotFoundException.class);

    assertThatThrownBy(() -> userService.updateUser(testUserId, userRequestDtoUpdate))
        .isInstanceOf(ResourceNotFoundException.class);
  }
}
