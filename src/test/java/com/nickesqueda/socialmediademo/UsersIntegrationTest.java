package com.nickesqueda.socialmediademo;

import static com.nickesqueda.socialmediademo.test.util.TestConstants.*;
import static org.assertj.core.api.Assertions.*;

import com.nickesqueda.socialmediademo.dto.UserRequestDto;
import com.nickesqueda.socialmediademo.dto.UserResponseDto;
import com.nickesqueda.socialmediademo.exception.ErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;

public class UsersIntegrationTest extends BaseIntegrationTest {

  @Test
  void getUser_ShouldReturnSuccessfulResponse_GivenValidId() {
    ResponseEntity<UserResponseDto> response =
        restTemplate.getForEntity(testUserUrl, UserResponseDto.class);

    assertSuccessfulResponseGetUser(response);
  }

  @Test
  void getUser_ShouldReturnSuccessfulResponse_WithAndWithoutAuthentication() {
    ResponseEntity<UserResponseDto> unauthenticatedResponse =
        restTemplate.getForEntity(testUserUrl, UserResponseDto.class);

    RequestEntity<Void> authenticatedRequestEntity =
        createAuthenticatedRequest(testUserUrl, null, HttpMethod.GET);
    ResponseEntity<UserResponseDto> authenticatedResponse =
        restTemplate.exchange(authenticatedRequestEntity, UserResponseDto.class);

    assertSuccessfulResponseGetUser(unauthenticatedResponse);
    assertSuccessfulResponseGetUser(authenticatedResponse);
  }

  @Test
  void getUser_ShouldReturn404WithErrorResponse_GivenUserDoesNotExist() {
    ResponseEntity<ErrorResponse> response =
        restTemplate.getForEntity(nonExistentUserUrl, ErrorResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody()).hasFieldOrProperty(ERROR_MESSAGE_PROPERTY_NAME);
  }

  @Test
  void updateUser_ShouldReturnSuccessfulResponse_GivenValidValues() {
    UserRequestDto requestBody =
        UserRequestDto.builder()
            .username("user1")
            .firstName(TEST_STRING)
            .lastName(TEST_STRING)
            .build();
    RequestEntity<UserRequestDto> requestEntity =
        createAuthenticatedRequest(testUserUrl, requestBody, HttpMethod.PUT);

    ResponseEntity<UserResponseDto> responseEntity =
        restTemplate.exchange(requestEntity, UserResponseDto.class);
    UserResponseDto responseBody = responseEntity.getBody();

    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(responseBody).isNotNull();
    assertThat(responseBody.getId()).isEqualTo(1);
    assertThat(responseBody.getUsername()).isEqualTo("user1");
    assertThat(responseBody.getFirstName()).isEqualTo(TEST_STRING);
    assertThat(responseBody.getLastName()).isEqualTo(TEST_STRING);
  }

  @Test
  void updateUser_ShouldReturn400_GivenBadRequest() {}

  @Test
  void updateUser_ShouldReturn401_GivenUnauthorizedUser() {}

  @Test
  void updateUser_ShouldReturn404_GivenUserNotFound() {}

  private void assertSuccessfulResponseGetUser(ResponseEntity<UserResponseDto> responseEntity) {
    UserResponseDto responseBody = responseEntity.getBody();
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(responseBody).isNotNull();
    assertThat(responseBody.getId()).isEqualTo(1);
    assertThat(responseBody.getUsername()).isEqualTo("user1");
  }
}
