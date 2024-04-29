package com.nickesqueda.socialmediademo;

import static com.nickesqueda.socialmediademo.test.util.TestConstants.*;
import static org.assertj.core.api.Assertions.*;

import com.nickesqueda.socialmediademo.dto.UserRequestDto;
import com.nickesqueda.socialmediademo.dto.UserResponseDto;
import com.nickesqueda.socialmediademo.exception.ErrorResponse;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;

public class UsersIntegrationTest extends BaseIntegrationTest {
  // TODO: reset user1 DB record to original state after each PUT test.

  private final UserRequestDto updateUserRequestBody =
      UserRequestDto.builder()
          .username("user1")
          .firstName(TEST_STRING)
          .lastName(TEST_STRING)
          .build();
  private final UserRequestDto updateUserBadRequestBody =
      UserRequestDto.builder()
          .username(null)
          .firstName("")
          .lastName("")
          .email("not an email")
          .phoneNumber("not a phone number")
          .birthday(LocalDate.MAX)
          .bio("")
          .build();

  @Test
  void getUser_ShouldReturnSuccessfulResponse_GivenValidId() {
    ResponseEntity<UserResponseDto> response =
        restTemplate.getForEntity(user1Url, UserResponseDto.class);

    assertSuccessfulResponseGetUser(response);
  }

  @Test
  void getUser_ShouldReturnSuccessfulResponse_WithAndWithoutAuthentication() {
    ResponseEntity<UserResponseDto> unauthenticatedResponse =
        restTemplate.getForEntity(user1Url, UserResponseDto.class);

    RequestEntity<Void> authenticatedRequestEntity =
        createAuthenticatedRequest("user1", user1Url, null, HttpMethod.GET);
    ResponseEntity<UserResponseDto> authenticatedResponse =
        restTemplate.exchange(authenticatedRequestEntity, UserResponseDto.class);

    assertSuccessfulResponseGetUser(unauthenticatedResponse);
    assertSuccessfulResponseGetUser(authenticatedResponse);
  }

  @Test
  void getUser_ShouldReturn404WithErrorResponse_GivenUserDoesNotExist() {
    ResponseEntity<ErrorResponse> responseEntity =
        restTemplate.getForEntity(nonExistentUserUrl, ErrorResponse.class);
    ErrorResponse responseBody = responseEntity.getBody();

    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    assertThat(responseBody).isNotNull();
    assertThat(responseBody.errorMessage()).isNotNull();
  }

  @Test
  void updateUser_ShouldReturnSuccessfulResponse_GivenValidValues() {
    RequestEntity<UserRequestDto> requestEntity =
        createAuthenticatedRequest("user1", user1Url, updateUserRequestBody, HttpMethod.PUT);

    ResponseEntity<UserResponseDto> responseEntity =
        restTemplate.exchange(requestEntity, UserResponseDto.class);
    UserResponseDto responseBody = responseEntity.getBody();

    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(responseBody).isNotNull();
    assertThat(responseBody.getId()).isEqualTo(1);
    assertThat(responseBody.getUsername()).isEqualTo(updateUserRequestBody.getUsername());
    assertThat(responseBody.getFirstName()).isEqualTo(updateUserRequestBody.getFirstName());
    assertThat(responseBody.getLastName()).isEqualTo(updateUserRequestBody.getLastName());
  }

  @Test
  void updateUser_ShouldReturn400WithErrorResponse_GivenBadRequest() {
    RequestEntity<UserRequestDto> requestEntity =
        createAuthenticatedRequest("user1", user1Url, updateUserBadRequestBody, HttpMethod.PUT);

    ResponseEntity<ErrorResponse> responseEntity =
        restTemplate.exchange(requestEntity, ErrorResponse.class);
    ErrorResponse responseBody = responseEntity.getBody();

    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(responseBody).isNotNull();
    assertThat(responseBody.errorMessage()).isNotNull();
    assertThat(responseBody.errorDetails().size()).isEqualTo(7);
  }

  @Test
  void updateUser_ShouldReturn401WithErrorResponse_GivenNoAuthorization() {
    // do not send Authorization header.
    RequestEntity<UserRequestDto> requestEntity =
        new RequestEntity<>(updateUserRequestBody, HttpMethod.PUT, user1Url);

    ResponseEntity<ErrorResponse> responseEntity =
        restTemplate.exchange(requestEntity, ErrorResponse.class);
    ErrorResponse responseBody = responseEntity.getBody();

    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    assertThat(responseBody).isNotNull();
    assertThat(responseBody.errorMessage()).isNotNull();
  }

  @Test
  void updateUser_ShouldReturn401WithErrorResponse_GivenUnauthorizedUser() {
    // send PUT request to user2 but with user1's authorization.
    RequestEntity<UserRequestDto> requestEntity =
        createAuthenticatedRequest("user1", user2Url, updateUserRequestBody, HttpMethod.PUT);

    ResponseEntity<ErrorResponse> responseEntity =
        restTemplate.exchange(requestEntity, ErrorResponse.class);
    ErrorResponse responseBody = responseEntity.getBody();

    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    assertThat(responseBody).isNotNull();
    assertThat(responseBody.errorMessage()).isNotNull();
  }

  private void assertSuccessfulResponseGetUser(ResponseEntity<UserResponseDto> responseEntity) {
    UserResponseDto responseBody = responseEntity.getBody();
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(responseBody).isNotNull();
    assertThat(responseBody.getId()).isEqualTo(1);
    assertThat(responseBody.getUsername()).isEqualTo("user1");
  }
}
