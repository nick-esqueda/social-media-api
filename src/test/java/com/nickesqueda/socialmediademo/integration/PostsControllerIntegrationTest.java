package com.nickesqueda.socialmediademo.integration;

import static com.nickesqueda.socialmediademo.test.util.TestConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nickesqueda.socialmediademo.entity.Post;
import com.nickesqueda.socialmediademo.repository.PostRepository;
import java.time.Instant;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

public class PostsControllerIntegrationTest extends BaseIntegrationTest {

  @Autowired private PostRepository postRepository;

  @Test
  void getPost_ShouldReturnSuccessfulResponse_GivenValidId() throws Exception {
    mockMvc
        .perform(get(postUriBuilder.buildAndExpand(postId).toUri()))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(postId))
        .andExpect(jsonPath("$.content").isNotEmpty());
  }

  @Test
  void getPost_ShouldReturn404WithErrorResponse_GivenPostDoesNotExist() throws Exception {
    mockMvc
        .perform(get(postUriBuilder.buildAndExpand(nonExistentPostId).toUri()))
        .andDo(print())
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.errorMessage").isNotEmpty());
  }

  @Test
  @WithMockUser
  @Transactional
  void updatePost_ShouldReturnSuccessfulResponse_GivenValidValues() throws Exception {
    when(authUtils.getCurrentAuthenticatedUserId()).thenReturn(userId);
    mockMvc
        .perform(
            put(postUriBuilder.buildAndExpand(postId).toUri())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatePostRequestJson))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(postId))
        .andExpect(jsonPath("$.content").value(TEST_STRING2));

    // "updatedAt" will not be updated in the PUT response, since this is a @Transactional test.
    // must validate updatedAt separately, as it gets updated only after the transaction
    // is committed (after test method end) or CHANGES ARE FLUSHED
    entityManager.flush();

    Post postEntity = postRepository.findById(postId).orElseThrow();
    assertThat(postEntity.getUpdatedAt()).isAfter(postEntity.getCreatedAt());
  }

  @Test
  @WithMockUser
  @Transactional
  void updatePost_ShouldBeReflectedByGetPost_GivenSuccessfulUpdate() throws Exception {
    when(authUtils.getCurrentAuthenticatedUserId()).thenReturn(userId);
    mockMvc
        .perform(
            put(postUriBuilder.buildAndExpand(postId).toUri())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatePostRequestJson))
        .andDo(print())
        .andExpect(status().isOk());

    // flush changes so that "updatedAt" is updated in the DB before querying again.
    // must be done when using @Transactional.
    entityManager.flush();

    String resultString =
        mockMvc
            .perform(get(postUriBuilder.buildAndExpand(postId).toUri()))
            .andDo(print())
            .andExpect(jsonPath("$.content").value(TEST_STRING2))
            .andReturn()
            .getResponse()
            .getContentAsString();

    Map<String, Instant> auditDates = extractAuditDates(resultString);
    assertThat(auditDates.get("updatedAt")).isAfter(auditDates.get("createdAt"));
  }

  @Test
  @WithMockUser
  @Transactional
  void updatePost_ShouldReturn400WithErrorResponse_GivenInvalidData() throws Exception {
    when(authUtils.getCurrentAuthenticatedUserId()).thenReturn(userId);
    mockMvc
        .perform(
            put(postUriBuilder.buildAndExpand(postId).toUri())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatePostBadRequestJson))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errorMessage").isNotEmpty());
  }

  @Test
  @WithMockUser
  @Transactional
  void updatePost_ShouldReturnEachValidationError_GivenInvalidData() throws Exception {
    mockMvc
        .perform(
            put(postUriBuilder.buildAndExpand(postId).toUri())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatePostBadRequestJson))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errorDetails", hasSize(1)));
  }

  @Test
  // @WithMockUser <- removed to simulate unauthenticated request.
  @Transactional
  void updatePost_ShouldReturn401_GivenNoAuthentication() throws Exception {
    mockMvc
        .perform(
            put(postUriBuilder.buildAndExpand(postId).toUri())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatePostRequestJson))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser
  @Transactional
  void updatePost_ShouldReturn403WithErrorResponse_GivenUnauthorizedUser() throws Exception {
    when(authUtils.getCurrentAuthenticatedUserId()).thenReturn(unauthorizedUserId);
    mockMvc
        .perform(
            put(postUriBuilder.buildAndExpand(postId).toUri())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatePostRequestJson))
        .andDo(print())
        .andExpect(status().isForbidden())
        .andExpect(jsonPath("$.errorMessage").isNotEmpty());
  }

  @Test
  @WithMockUser
  @Transactional
  void updatePost_ShouldReturn404WithErrorResponse_GivenPostDoesNotExist() throws Exception {
    mockMvc
        .perform(
            put(postUriBuilder.buildAndExpand(nonExistentPostId).toUri())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatePostRequestJson))
        .andDo(print())
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.errorMessage").isNotEmpty());
  }

  @Test
  @WithMockUser
  @Transactional
  void createComment_ShouldReturnSuccessfulResponse_GivenValidValues() throws Exception {
    when(authUtils.getCurrentAuthenticatedUserId()).thenReturn(userId);
    mockMvc
        .perform(
            post(postsCommentsUriBuilder.buildAndExpand(postId).toUri())
                .contentType(MediaType.APPLICATION_JSON)
                .content(createCommentRequestJson))
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.content").value(TEST_STRING));
  }

  @Test
  @WithMockUser
  @Transactional
  void createComment_ShouldBeReflectedByGetPostsComments_GivenSuccessfulCreate() throws Exception {
    mockMvc
        .perform(get(postsCommentsUriBuilder.buildAndExpand(postId).toUri()))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)));
    when(authUtils.getCurrentAuthenticatedUserId()).thenReturn(userId);

    mockMvc
        .perform(
            post(postsCommentsUriBuilder.buildAndExpand(postId).toUri())
                .contentType(MediaType.APPLICATION_JSON)
                .content(createCommentRequestJson))
        .andDo(print())
        .andExpect(status().isCreated());

    mockMvc
        .perform(get(postsCommentsUriBuilder.buildAndExpand(postId).toUri()))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)));
  }

  @Test
  @WithMockUser
  @Transactional
  void createComment_ShouldReturn400WithErrorResponse_GivenInvalidData() throws Exception {
    when(authUtils.getCurrentAuthenticatedUserId()).thenReturn(userId);
    mockMvc
        .perform(
            post(postsCommentsUriBuilder.buildAndExpand(postId).toUri())
                .contentType(MediaType.APPLICATION_JSON)
                .content(createCommentBadRequestJson))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errorMessage").isNotEmpty());
  }

  @Test
  @WithMockUser
  @Transactional
  void createComment_ShouldReturnEachValidationError_GivenInvalidData() throws Exception {
    mockMvc
        .perform(
            post(postsCommentsUriBuilder.buildAndExpand(postId).toUri())
                .contentType(MediaType.APPLICATION_JSON)
                .content(createCommentBadRequestJson))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errorDetails", hasSize(1)));
  }

  @Test
  // @WithMockUser <- removed to simulate unauthenticated request.
  @Transactional
  void createComment_ShouldReturn401_GivenNoAuthentication() throws Exception {
    when(authUtils.getCurrentAuthenticatedUserId()).thenReturn(userId);
    mockMvc
        .perform(
            post(postsCommentsUriBuilder.buildAndExpand(postId).toUri())
                .contentType(MediaType.APPLICATION_JSON)
                .content(createCommentRequestJson))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser
  @Transactional
  void createComment_ShouldReturn404WithErrorResponse_GivenPostDoesNotExist() throws Exception {
    mockMvc
        .perform(
            post(postsCommentsUriBuilder.buildAndExpand(nonExistentPostId).toUri())
                .contentType(MediaType.APPLICATION_JSON)
                .content(createCommentRequestJson))
        .andDo(print())
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.errorMessage").isNotEmpty());
  }

  @Test
  @WithMockUser
  @Transactional
  void deletePost_ShouldReturnSuccessfulResponse_GivenValidId() throws Exception {
    when(authUtils.getCurrentAuthenticatedUserId()).thenReturn(userId);
    mockMvc
        .perform(delete(postUriBuilder.buildAndExpand(postId).toUri()))
        .andDo(print())
        .andExpect(status().isNoContent());
  }

  @Test
  @WithMockUser
  @Transactional
  void deletePost_ShouldBeReflectedByGetUsersPosts_GivenSuccessfulDelete() throws Exception {
    mockMvc
        .perform(get(usersPostsUriBuilder.buildAndExpand(userId).toUri()))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)));
    when(authUtils.getCurrentAuthenticatedUserId()).thenReturn(userId);

    mockMvc
        .perform(delete(postUriBuilder.buildAndExpand(postId).toUri()))
        .andDo(print())
        .andExpect(status().isNoContent());

    mockMvc
        .perform(get(usersPostsUriBuilder.buildAndExpand(userId).toUri()))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(0)));
  }

  @Test
  // @WithMockUser <- removed to simulate unauthenticated request.
  @Transactional
  void deletePost_ShouldReturn401_GivenNoAuthentication() throws Exception {
    when(authUtils.getCurrentAuthenticatedUserId()).thenReturn(userId);
    mockMvc
        .perform(delete(postUriBuilder.buildAndExpand(postId).toUri()))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser
  @Transactional
  void deletePost_ShouldReturn403WithErrorResponse_GivenUnauthorizedUser() throws Exception {
    when(authUtils.getCurrentAuthenticatedUserId()).thenReturn(unauthorizedUserId);
    mockMvc
        .perform(delete(postUriBuilder.buildAndExpand(postId).toUri()))
        .andDo(print())
        .andExpect(status().isForbidden())
        .andExpect(jsonPath("$.errorMessage").isNotEmpty());
  }

  @Test
  @WithMockUser
  @Transactional
  void deletePost_ShouldReturn404WithErrorResponse_GivenPostDoesNotExist() throws Exception {
    when(authUtils.getCurrentAuthenticatedUserId()).thenReturn(userId);
    mockMvc
        .perform(delete(postUriBuilder.buildAndExpand(nonExistentPostId).toUri()))
        .andDo(print())
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.errorMessage").isNotEmpty());
  }

  @Test
  @WithMockUser
  @Transactional
  void deletePostsComments_ShouldReturnSuccessfulResponse_GivenValidId() throws Exception {
    when(authUtils.getCurrentAuthenticatedUserId()).thenReturn(userId);
    mockMvc
        .perform(delete(postsCommentsUriBuilder.buildAndExpand(postId).toUri()))
        .andDo(print())
        .andExpect(status().isNoContent());
  }

  @Test
  @WithMockUser
  @Transactional
  void deletePostsComments_ShouldBeReflectedByGetPostsComments_GivenSuccessfulDelete()
      throws Exception {

    mockMvc
        .perform(get(postsCommentsUriBuilder.buildAndExpand(postId).toUri()))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)));
    when(authUtils.getCurrentAuthenticatedUserId()).thenReturn(userId);

    mockMvc
        .perform(delete(postsCommentsUriBuilder.buildAndExpand(postId).toUri()))
        .andDo(print())
        .andExpect(status().isNoContent());

    mockMvc
        .perform(get(postsCommentsUriBuilder.buildAndExpand(postId).toUri()))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(0)));
  }

  @Test
  // @WithMockUser <- removed to simulate unauthenticated request.
  @Transactional
  void deletePostsComments_ShouldReturn401_GivenNoAuthentication() throws Exception {
    when(authUtils.getCurrentAuthenticatedUserId()).thenReturn(userId);
    mockMvc
        .perform(delete(postsCommentsUriBuilder.buildAndExpand(postId).toUri()))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser
  @Transactional
  void deletePostsComments_ShouldReturn403WithErrorResponse_GivenUnauthorizedUser()
      throws Exception {
    when(authUtils.getCurrentAuthenticatedUserId()).thenReturn(unauthorizedUserId);
    mockMvc
        .perform(delete(postsCommentsUriBuilder.buildAndExpand(postId).toUri()))
        .andDo(print())
        .andExpect(status().isForbidden())
        .andExpect(jsonPath("$.errorMessage").isNotEmpty());
  }

  @Test
  @WithMockUser
  @Transactional
  void deletePostsComments_ShouldReturn404WithErrorResponse_GivenPostDoesNotExist()
      throws Exception {
    when(authUtils.getCurrentAuthenticatedUserId()).thenReturn(userId);
    mockMvc
        .perform(delete(postsCommentsUriBuilder.buildAndExpand(nonExistentPostId).toUri()))
        .andDo(print())
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.errorMessage").isNotEmpty());
  }
}
