package com.nickesqueda.socialmediademo;

import static com.nickesqueda.socialmediademo.test.util.TestConstants.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nickesqueda.socialmediademo.dto.UserRequestDto;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

public class UsersIntegrationTest extends BaseIntegrationTest {

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
  void getUser_ShouldReturnSuccessfulResponse_GivenValidId() throws Exception {
    mockMvc
        .perform(get(user1Url))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.username").value("user1"));
  }

  @Test
  void getUser_ShouldReturn404WithErrorResponse_GivenUserDoesNotExist() throws Exception {
    mockMvc
        .perform(get(nonExistentUserUrl))
        .andDo(print())
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.errorMessage").isNotEmpty());
  }

  @Test
  @WithMockUser
  @Transactional
  void updateUser_ShouldReturnSuccessfulResponse_GivenValidValues() throws Exception {
    when(authUtils.getCurrentAuthenticatedUserId()).thenReturn(1L);
    mockMvc
        .perform(
            put(user1Url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateUserRequestBody)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.username").value(updateUserRequestBody.getUsername()))
        .andExpect(jsonPath("$.firstName").value(updateUserRequestBody.getFirstName()))
        .andExpect(jsonPath("$.lastName").value(updateUserRequestBody.getLastName()));
  }

  @Test
  @WithMockUser
  @Transactional
  void updateUser_ShouldReturn400WithErrorResponse_GivenBadRequest() throws Exception {
    when(authUtils.getCurrentAuthenticatedUserId()).thenReturn(1L);
    mockMvc
        .perform(
            put(user1Url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateUserBadRequestBody)))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errorMessage").isNotEmpty())
        .andExpect(jsonPath("$.errorDetails").isNotEmpty());
  }

  @Test
  @WithMockUser
  @Transactional
  void updateUser_ShouldReturn401WithErrorResponse_GivenUnauthorizedUser() throws Exception {
    when(authUtils.getCurrentAuthenticatedUserId()).thenReturn(2L);
    mockMvc
        .perform(
            put(user1Url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateUserRequestBody)))
        .andDo(print())
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.errorMessage").isNotEmpty());
  }
}
