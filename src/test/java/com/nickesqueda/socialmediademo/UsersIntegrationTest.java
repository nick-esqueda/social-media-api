package com.nickesqueda.socialmediademo;

import static com.nickesqueda.socialmediademo.test.util.TestConstants.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nickesqueda.socialmediademo.dto.PostRequestDto;
import com.nickesqueda.socialmediademo.dto.UserRequestDto;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

public class UsersIntegrationTest extends BaseIntegrationTest {

  private final UserRequestDto updateUserRequest =
      UserRequestDto.builder()
          .username("user1")
          .firstName(TEST_STRING)
          .lastName(TEST_STRING)
          .build();
  private final UserRequestDto updateUserBadRequest =
      UserRequestDto.builder()
          .username(null)
          .firstName("")
          .lastName("")
          .email("not an email")
          .phoneNumber("not a phone number")
          .birthday(LocalDate.MAX)
          .bio("")
          .build();
  private final PostRequestDto createPostRequest =
      PostRequestDto.builder().content(TEST_STRING).build();
  private final PostRequestDto createPostBadRequest = PostRequestDto.builder().content("").build();

  @Test
  void getUser_ShouldReturnSuccessfulResponse_GivenValidId() throws Exception {
    mockMvc
        .perform(get(userUriBuilder.buildAndExpand(userId).toUri()))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.username").value("user1"));
  }

  @Test
  void getUser_ShouldReturn404WithErrorResponse_GivenUserDoesNotExist() throws Exception {
    mockMvc
        .perform(get(userUriBuilder.buildAndExpand(nonExistentUserId).toUri()))
        .andDo(print())
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.errorMessage").isNotEmpty());
  }

  @Test
  @WithMockUser
  @Transactional
  void updateUser_ShouldReturnSuccessfulResponse_GivenValidValues() throws Exception {
    when(authUtils.getCurrentAuthenticatedUserId()).thenReturn(userId);
    mockMvc
        .perform(
            put(userUriBuilder.buildAndExpand(userId).toUri())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateUserRequest)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.username").value(updateUserRequest.getUsername()))
        .andExpect(jsonPath("$.firstName").value(updateUserRequest.getFirstName()))
        .andExpect(jsonPath("$.lastName").value(updateUserRequest.getLastName()));
  }

  @Test
  @WithMockUser
  @Transactional
  void updateUser_ShouldReturn400WithErrorResponse_GivenBadRequest() throws Exception {
    when(authUtils.getCurrentAuthenticatedUserId()).thenReturn(userId);
    mockMvc
        .perform(
            put(userUriBuilder.buildAndExpand(userId).toUri())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateUserBadRequest)))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errorMessage").isNotEmpty())
        .andExpect(jsonPath("$.errorDetails").isNotEmpty());
  }

  @Test
  @WithMockUser
  @Transactional
  void updateUser_ShouldReturn403WithErrorResponse_GivenUnauthorizedUser() throws Exception {
    when(authUtils.getCurrentAuthenticatedUserId()).thenReturn(unauthorizedUserId);
    mockMvc
        .perform(
            put(userUriBuilder.buildAndExpand(userId).toUri())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateUserRequest)))
        .andDo(print())
        .andExpect(status().isForbidden())
        .andExpect(jsonPath("$.errorMessage").isNotEmpty());
  }

  @Test
  @WithMockUser
  @Transactional
  void updateUser_ShouldReturn404WithErrorResponse_GivenUserDoesNotExist() throws Exception {
    when(authUtils.getCurrentAuthenticatedUserId()).thenReturn(unauthorizedUserId);
    mockMvc
        .perform(
            put(userUriBuilder.buildAndExpand(nonExistentUserId).toUri())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateUserRequest)))
        .andDo(print())
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.errorMessage").isNotEmpty());
  }

  @Test
  void getUsersPosts_ShouldReturnSuccessfulResponse_GivenValidId() throws Exception {
    mockMvc
        .perform(get(usersPostsUriBuilder.buildAndExpand(userId).toUri()))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)));
  }

  @Test
  void getUsersPosts_ShouldReturn404WithErrorResponse_GivenUserDoesNotExist() throws Exception {
    mockMvc
        .perform(get(userUriBuilder.buildAndExpand(nonExistentUserId).toUri()))
        .andDo(print())
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.errorMessage").isNotEmpty());
  }

  @Test
  @WithMockUser
  @Transactional
  void createPost_ShouldReturnSuccessfulResponse_GivenValidValues() throws Exception {
    when(authUtils.getCurrentAuthenticatedUserId()).thenReturn(userId);
    mockMvc
        .perform(
            post(usersPostsUriBuilder.buildAndExpand(userId).toUri())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createPostRequest)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.content").value(createPostRequest.getContent()));
  }

  @Test
  @WithMockUser
  @Transactional
  void createPost_ShouldReturn400WithErrorResponse_GivenBadRequest() throws Exception {
    when(authUtils.getCurrentAuthenticatedUserId()).thenReturn(userId);
    mockMvc
        .perform(
            post(usersPostsUriBuilder.buildAndExpand(userId).toUri())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createPostBadRequest)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errorMessage").isNotEmpty());
  }

  @Test
  @WithMockUser
  @Transactional
  void createPost_ShouldReturn403WithErrorResponse_GivenUnauthorizedUser() throws Exception {
    when(authUtils.getCurrentAuthenticatedUserId()).thenReturn(unauthorizedUserId);
    mockMvc
        .perform(
            post(usersPostsUriBuilder.buildAndExpand(userId).toUri())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createPostRequest)))
        .andExpect(status().isForbidden())
        .andExpect(jsonPath("$.errorMessage").isNotEmpty());
  }

  @Test
  @WithMockUser
  @Transactional
  void createPost_ShouldReturn404WithErrorResponse_GivenUserDoesNotExist() throws Exception {
    when(authUtils.getCurrentAuthenticatedUserId()).thenReturn(userId);
    mockMvc
        .perform(
            post(usersPostsUriBuilder.buildAndExpand(nonExistentUserId).toUri())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createPostRequest)))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.errorMessage").isNotEmpty());
  }
}
