package com.nickesqueda.socialmediademo.integration;

import static com.nickesqueda.socialmediademo.test.util.TestConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nickesqueda.socialmediademo.entity.UserEntity;
import java.time.Instant;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

public class UsersControllerIntegrationTest extends BaseIntegrationTest {

  @Test
  void getUser_ShouldReturnSuccessfulResponse_GivenValidId() throws Exception {
    mockMvc
        .perform(get(userUriBuilder.buildAndExpand(userId).toUri()))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(userId))
        .andExpect(jsonPath("$.username").value(USER1_USERNAME));
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
                .content(updateUserRequestJson))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(userId))
        .andExpect(jsonPath("$.username").value(USER1_USERNAME))
        .andExpect(jsonPath("$.firstName").value(TEST_STRING))
        .andExpect(jsonPath("$.lastName").value(TEST_STRING))
        .andReturn();

    // "updatedAt" will not be updated in the PUT response, since this is a @Transactional test.
    // must validate updatedAt separately, as it gets updated only after the transaction
    // is committed (after test method end) or CHANGES ARE FLUSHED
    entityManager.flush();

    UserEntity userEntity = userRepository.findById(userId).orElseThrow();
    assertThat(userEntity.getUpdatedAt()).isAfter(userEntity.getCreatedAt());
  }

  @Test
  @WithMockUser
  @Transactional
  void updateUser_ShouldBeReflectedByGetUser_GivenSuccessfulUpdate() throws Exception {
    when(authUtils.getCurrentAuthenticatedUserId()).thenReturn(userId);
    mockMvc
        .perform(
            put(userUriBuilder.buildAndExpand(userId).toUri())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateUserRequestJson))
        .andDo(print())
        .andExpect(status().isOk());

    // flush changes so that "updatedAt" is updated in the DB before querying again.
    // must be done when using @Transactional.
    entityManager.flush();

    String result =
        mockMvc
            .perform(get(userUriBuilder.buildAndExpand(userId).toUri()))
            .andDo(print())
            .andExpect(jsonPath("$.id").value(userId))
            .andExpect(jsonPath("$.username").value(USER1_USERNAME))
            .andExpect(jsonPath("$.firstName").value(TEST_STRING))
            .andExpect(jsonPath("$.lastName").value(TEST_STRING))
            .andReturn()
            .getResponse()
            .getContentAsString();

    Map<String, Instant> auditDates = extractAuditDates(result);
    assertThat(auditDates.get("updatedAt")).isAfter(auditDates.get("createdAt"));
  }

  @Test
  @WithMockUser
  @Transactional
  void updateUser_ShouldReturn400WithErrorResponse_GivenInvalidData() throws Exception {
    when(authUtils.getCurrentAuthenticatedUserId()).thenReturn(userId);
    mockMvc
        .perform(
            put(userUriBuilder.buildAndExpand(userId).toUri())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateUserBadRequestJson))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errorMessage").isNotEmpty());
  }

  @Test
  @WithMockUser
  @Transactional
  void updateUser_ShouldReturnEachValidationError_GivenInvalidData() throws Exception {
    when(authUtils.getCurrentAuthenticatedUserId()).thenReturn(userId);
    mockMvc
        .perform(
            put(userUriBuilder.buildAndExpand(userId).toUri())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateUserBadRequestJson))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errorDetails", hasSize(8)));
  }

  @Test
  // @WithMockUser <- removed to simulate unauthenticated request.
  @Transactional
  void updateUser_ShouldReturn401_GivenNoAuthentication() throws Exception {
    when(authUtils.getCurrentAuthenticatedUserId()).thenReturn(userId);
    mockMvc
        .perform(
            put(userUriBuilder.buildAndExpand(userId).toUri())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateUserRequestJson))
        .andDo(print())
        .andExpect(status().isUnauthorized());
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
                .content(updateUserRequestJson))
        .andDo(print())
        .andExpect(status().isForbidden())
        .andExpect(jsonPath("$.errorMessage").isNotEmpty());
  }

  @Test
  @WithMockUser
  @Transactional
  void updateUser_ShouldNotModifyDb_GivenUnauthorizedUser() throws Exception {
    String userBeforeRequest = userRepository.findById(userId).orElseThrow().toString();
    when(authUtils.getCurrentAuthenticatedUserId()).thenReturn(unauthorizedUserId);

    mockMvc
        .perform(
            put(userUriBuilder.buildAndExpand(userId).toUri())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateUserRequestJson))
        .andDo(print())
        .andExpect(status().isForbidden());
    String userAfterRequest = userRepository.findById(userId).orElseThrow().toString();

    assertThat(userBeforeRequest).isEqualTo(userAfterRequest);
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
                .content(updateUserRequestJson))
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
        .andExpect(jsonPath("$", hasSize(usersPostsCount)));
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
  void deleteUsersPosts_ShouldReturnSuccessfulResponse_GivenValidId() throws Exception {
    when(authUtils.getCurrentAuthenticatedUserId()).thenReturn(userId);
    mockMvc
        .perform(delete(usersPostsUriBuilder.buildAndExpand(userId).toUri()))
        .andDo(print())
        .andExpect(status().isNoContent());
  }

  @Test
  @WithMockUser
  @Transactional
  void deleteUsersPosts_ShouldBeReflectedByGetUsersPosts_GivenSuccessfulDelete() throws Exception {
    mockMvc
        .perform(get(usersPostsUriBuilder.buildAndExpand(userId).toUri()))
        .andExpect(jsonPath("$", hasSize(usersPostsCount)));
    when(authUtils.getCurrentAuthenticatedUserId()).thenReturn(userId);

    mockMvc
        .perform(delete(usersPostsUriBuilder.buildAndExpand(userId).toUri()))
        .andDo(print())
        .andExpect(status().isNoContent());

    mockMvc
        .perform(get(usersPostsUriBuilder.buildAndExpand(userId).toUri()))
        .andDo(print())
        .andExpect(jsonPath("$", hasSize(0)));
  }

  @Test
  // @WithMockUser <- removed to simulate unauthenticated request.
  @Transactional
  void deleteUsersPosts_ShouldReturn401_GivenNoAuthentication() throws Exception {
    when(authUtils.getCurrentAuthenticatedUserId()).thenReturn(userId);
    mockMvc
        .perform(delete(usersPostsUriBuilder.buildAndExpand(userId).toUri()))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser
  @Transactional
  void deleteUsersPosts_ShouldReturn403WithErrorResponse_GivenUnauthorizedUser() throws Exception {
    when(authUtils.getCurrentAuthenticatedUserId()).thenReturn(unauthorizedUserId);
    mockMvc
        .perform(delete(usersPostsUriBuilder.buildAndExpand(userId).toUri()))
        .andDo(print())
        .andExpect(status().isForbidden())
        .andExpect(jsonPath("$.errorMessage").isNotEmpty());
  }

  @Test
  @WithMockUser
  @Transactional
  void deleteUsersPosts_ShouldNotModifyDb_GivenUnauthorizedUser() throws Exception {
    int numUsersPostsBeforeRequest = postRepository.findByUserId(userId).size();
    when(authUtils.getCurrentAuthenticatedUserId()).thenReturn(unauthorizedUserId);

    mockMvc
        .perform(delete(usersPostsUriBuilder.buildAndExpand(userId).toUri()))
        .andDo(print())
        .andExpect(status().isForbidden());
    int numUsersPostsAfterRequest = postRepository.findByUserId(userId).size();

    assertThat(numUsersPostsBeforeRequest).isEqualTo(numUsersPostsAfterRequest);
  }

  @Test
  @WithMockUser
  @Transactional
  void deleteUsersPosts_ShouldReturn404WithErrorResponse_GivenUserDoesNotExist() throws Exception {
    when(authUtils.getCurrentAuthenticatedUserId()).thenReturn(userId);
    mockMvc
        .perform(delete(usersPostsUriBuilder.buildAndExpand(nonExistentUserId).toUri()))
        .andDo(print())
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.errorMessage").isNotEmpty());
  }

  @Test
  void getUsersComments_ShouldReturnSuccessfulResponse_GivenValidId() throws Exception {
    mockMvc
        .perform(get(usersCommentsUriBuilder.buildAndExpand(userId).toUri()))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(usersCommentsCount)));
  }

  @Test
  void getUsersComments_ShouldReturn404WithErrorResponse_GivenUserDoesNotExist() throws Exception {
    mockMvc
        .perform(get(usersCommentsUriBuilder.buildAndExpand(nonExistentUserId).toUri()))
        .andDo(print())
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.errorMessage").isNotEmpty());
  }

  @Test
  @WithMockUser
  @Transactional
  void deleteUsersComments_ShouldReturnSuccessfulResponse_GivenValidId() throws Exception {
    when(authUtils.getCurrentAuthenticatedUserId()).thenReturn(userId);
    mockMvc
        .perform(delete(usersCommentsUriBuilder.buildAndExpand(userId).toUri()))
        .andDo(print())
        .andExpect(status().isNoContent());
  }

  @Test
  @WithMockUser
  @Transactional
  void deleteUsersComments_ShouldBeReflectedByGetUsersComments_GivenSuccessfulDelete()
      throws Exception {

    mockMvc
        .perform(get(usersCommentsUriBuilder.buildAndExpand(userId).toUri()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(usersCommentsCount)));
    when(authUtils.getCurrentAuthenticatedUserId()).thenReturn(userId);

    mockMvc
        .perform(delete(usersCommentsUriBuilder.buildAndExpand(userId).toUri()))
        .andDo(print())
        .andExpect(status().isNoContent());

    mockMvc
        .perform(get(usersCommentsUriBuilder.buildAndExpand(userId).toUri()))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(0)));
  }

  @Test
  // @WithMockUser <- removed to simulate unauthenticated request.
  @Transactional
  void deleteUsersComments_ShouldReturn401_GivenNoAuthentication() throws Exception {
    when(authUtils.getCurrentAuthenticatedUserId()).thenReturn(userId);
    mockMvc
        .perform(delete(usersCommentsUriBuilder.buildAndExpand(userId).toUri()))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser
  @Transactional
  void deleteUsersComments_ShouldReturn403WithErrorResponse_GivenUnauthorizedUser()
      throws Exception {
    when(authUtils.getCurrentAuthenticatedUserId()).thenReturn(unauthorizedUserId);
    mockMvc
        .perform(delete(usersCommentsUriBuilder.buildAndExpand(userId).toUri()))
        .andDo(print())
        .andExpect(status().isForbidden())
        .andExpect(jsonPath("$.errorMessage").isNotEmpty());
  }

  @Test
  @WithMockUser
  @Transactional
  void deleteUsersComments_ShouldNotModifyDb_GivenUnauthorizedUser() throws Exception {
    int numUsersCommentsBeforeRequest = commentRepository.findByUserId(userId).size();
    when(authUtils.getCurrentAuthenticatedUserId()).thenReturn(unauthorizedUserId);

    mockMvc
        .perform(delete(usersCommentsUriBuilder.buildAndExpand(userId).toUri()))
        .andDo(print())
        .andExpect(status().isForbidden());
    int numUsersCommentsAfterRequest = commentRepository.findByUserId(userId).size();

    assertThat(numUsersCommentsBeforeRequest).isEqualTo(numUsersCommentsAfterRequest);
  }

  @Test
  @WithMockUser
  @Transactional
  void deleteUsersComments_ShouldReturn404WithErrorResponse_GivenUserDoesNotExist()
      throws Exception {
    when(authUtils.getCurrentAuthenticatedUserId()).thenReturn(userId);
    mockMvc
        .perform(delete(usersCommentsUriBuilder.buildAndExpand(nonExistentUserId).toUri()))
        .andDo(print())
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.errorMessage").isNotEmpty());
  }
}
