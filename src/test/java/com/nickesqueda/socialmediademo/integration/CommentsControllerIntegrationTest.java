package com.nickesqueda.socialmediademo.integration;

import static com.nickesqueda.socialmediademo.test.util.TestConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.jayway.jsonpath.JsonPath;
import com.nickesqueda.socialmediademo.entity.Comment;
import java.time.Instant;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

public class CommentsControllerIntegrationTest extends BaseIntegrationTest {

  @Test
  void getComment_ShouldReturnSuccessfulResponse_GivenValidId() throws Exception {
    mockMvc
        .perform(get(commentUriBuilder.buildAndExpand(commentId).toUri()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(commentId))
        .andExpect(jsonPath("$.content").isNotEmpty());
  }

  @Test
  void getComment_ShouldReturn404WithErrorResponse_GivenCommentDoesNotExist() throws Exception {
    mockMvc
        .perform(get(commentUriBuilder.buildAndExpand(nonExistentCommentId).toUri()))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.errorMessage").isNotEmpty());
  }

  @Test
  @WithMockUser
  @Transactional
  void updateComment_ShouldReturnSuccessfulResponse_GivenValidValues() throws Exception {
    when(authUtils.getCurrentAuthenticatedUserId()).thenReturn(userId);
    mockMvc
        .perform(
            put(commentUriBuilder.buildAndExpand(commentId).toUri())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateCommentRequestJson))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(commentId))
        .andExpect(jsonPath("$.content").value(TEST_STRING2));

    // "updatedAt" will not be updated in the PUT response, since this is a @Transactional test.
    // must validate updatedAt separately, as it gets updated only after the transaction
    // is committed (after test method end) or CHANGES ARE FLUSHED
    entityManager.flush();

    Comment commentEntity = commentRepository.findById(commentId).orElseThrow();
    assertThat(commentEntity.getUpdatedAt()).isAfter(commentEntity.getCreatedAt());
  }

  @Test
  @WithMockUser
  @Transactional
  void updateComment_ShouldBeReflectedByGetComment_GivenSuccessfulUpdate() throws Exception {
    when(authUtils.getCurrentAuthenticatedUserId()).thenReturn(userId);
    mockMvc
        .perform(
            put(commentUriBuilder.buildAndExpand(commentId).toUri())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateCommentRequestJson))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").value(TEST_STRING2));

    // flush changes so that "updatedAt" is updated in the DB before querying again.
    // must be done when using @Transactional.
    entityManager.flush();

    String resultString =
        mockMvc
            .perform(get(commentUriBuilder.buildAndExpand(commentId).toUri()))
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
  void updateComment_ShouldReturn400WithErrorResponse_GivenInvalidData() throws Exception {
    when(authUtils.getCurrentAuthenticatedUserId()).thenReturn(userId);
    mockMvc
        .perform(
            put(commentUriBuilder.buildAndExpand(commentId).toUri())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateCommentBadRequestJson))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errorMessage").isNotEmpty());
  }

  @Test
  @WithMockUser
  @Transactional
  void updateComment_ShouldReturnEachValidationError_GivenInvalidData() throws Exception {
    mockMvc
        .perform(
            put(commentUriBuilder.buildAndExpand(commentId).toUri())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateCommentBadRequestJson))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errorDetails", hasSize(1)));
  }

  @Test
  // @WithMockUser removed to simulate unauthenticated request
  @Transactional
  void updateComment_ShouldReturn401_GivenNoAuthentication() throws Exception {
    mockMvc
        .perform(
            put(commentUriBuilder.buildAndExpand(commentId).toUri())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateCommentRequestJson))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser
  @Transactional
  void updateComment_ShouldReturn403WithErrorResponse_GivenUnauthorizedUser() throws Exception {
    when(authUtils.getCurrentAuthenticatedUserId()).thenReturn(unauthorizedUserId);
    mockMvc
        .perform(
            put(commentUriBuilder.buildAndExpand(commentId).toUri())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateCommentRequestJson))
        .andExpect(status().isForbidden())
        .andExpect(jsonPath("$.errorMessage").isNotEmpty());
  }

  @Test
  @WithMockUser
  @Transactional
  void updateComment_ShouldNotModifyDb_GivenUnauthorizedUser() throws Exception {
    String commentBeforeRequest = commentRepository.findById(commentId).orElseThrow().toString();
    when(authUtils.getCurrentAuthenticatedUserId()).thenReturn(unauthorizedUserId);

    mockMvc
        .perform(
            put(commentUriBuilder.buildAndExpand(commentId).toUri())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateCommentRequestJson))
        .andDo(print())
        .andExpect(status().isForbidden());
    String commentAfterRequest = commentRepository.findById(commentId).orElseThrow().toString();

    assertThat(commentBeforeRequest).isEqualTo(commentAfterRequest);
  }

  @Test
  @WithMockUser
  @Transactional
  void updateComment_ShouldReturn404WithErrorResponse_GivenCommentDoesNotExist() throws Exception {
    when(authUtils.getCurrentAuthenticatedUserId()).thenReturn(userId);
    mockMvc
        .perform(
            put(commentUriBuilder.buildAndExpand(nonExistentCommentId).toUri())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateCommentRequestJson))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.errorMessage").isNotEmpty());
  }

  @Test
  @WithMockUser
  @Transactional
  void deleteComment_ShouldReturnSuccessfulResponse_GivenValidId() throws Exception {
    when(authUtils.getCurrentAuthenticatedUserId()).thenReturn(userId);
    mockMvc
        .perform(delete(commentUriBuilder.buildAndExpand(commentId).toUri()))
        .andExpect(status().isNoContent());
  }

  @Test
  @WithMockUser
  @Transactional
  void deleteComment_ShouldBeReflectedByGetUsersComments_GivenSuccessfulDelete() throws Exception {
    String response =
        mockMvc
            .perform(get(usersCommentsUriBuilder.buildAndExpand(userId).toUri()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(usersCommentsCount)))
            .andReturn()
            .getResponse()
            .getContentAsString();
    Long usersCommentId = ((Integer) JsonPath.read(response, "$[0].id")).longValue();

    when(authUtils.getCurrentAuthenticatedUserId()).thenReturn(userId);

    mockMvc
        .perform(delete(commentUriBuilder.buildAndExpand(usersCommentId).toUri()))
        .andExpect(status().isNoContent());

    mockMvc
        .perform(get(usersCommentsUriBuilder.buildAndExpand(userId).toUri()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(usersCommentsCount - 1)));
  }

  @Test
  @WithMockUser
  @Transactional
  void deleteComment_ShouldBeReflectedByGetPostsComments_GivenSuccessfulDelete() throws Exception {
    String response =
        mockMvc
            .perform(get(postsCommentsUriBuilder.buildAndExpand(postId).toUri()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(postsCommentsCount)))
            .andReturn()
            .getResponse()
            .getContentAsString();

    // get the post's first comment's userId to authenticate before deleting.
    Long postsCommentId = ((Integer) JsonPath.read(response, "$[0].id")).longValue();
    Comment commentEntity = commentRepository.findById(postsCommentId).orElseThrow();
    Long commentsUserId = commentEntity.getUser().getId();

    when(authUtils.getCurrentAuthenticatedUserId()).thenReturn(commentsUserId);

    mockMvc
        .perform(delete(commentUriBuilder.buildAndExpand(postsCommentId).toUri()))
        .andExpect(status().isNoContent());

    mockMvc
        .perform(get(postsCommentsUriBuilder.buildAndExpand(postId).toUri()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(postsCommentsCount - 1)));
  }

  @Test
  // @WithMockUser removed to simulate unauthenticated request
  @Transactional
  void deleteComment_ShouldReturn401_GivenNoAuthentication() throws Exception {
    mockMvc
        .perform(delete(commentUriBuilder.buildAndExpand(commentId).toUri()))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser
  @Transactional
  void deleteComment_ShouldReturn403WithErrorResponse_GivenUnauthorizedUser() throws Exception {
    when(authUtils.getCurrentAuthenticatedUserId()).thenReturn(unauthorizedUserId);
    mockMvc
        .perform(delete(commentUriBuilder.buildAndExpand(commentId).toUri()))
        .andExpect(status().isForbidden())
        .andExpect(jsonPath("$.errorMessage").isNotEmpty());
  }

  @Test
  @WithMockUser
  @Transactional
  void deleteComment_ShouldNotModifyDb_GivenUnauthorizedUser() throws Exception {
    long numCommentsBeforeRequest = commentRepository.count();
    when(authUtils.getCurrentAuthenticatedUserId()).thenReturn(unauthorizedUserId);

    mockMvc
        .perform(delete(commentUriBuilder.buildAndExpand(commentId).toUri()))
        .andExpect(status().isForbidden());
    long numCommentsAfterRequest = commentRepository.count();

    assertThat(numCommentsBeforeRequest).isEqualTo(numCommentsAfterRequest);
  }

  @Test
  @WithMockUser
  @Transactional
  void deleteComment_ShouldReturn404WithErrorResponse_GivenCommentDoesNotExist() throws Exception {
    when(authUtils.getCurrentAuthenticatedUserId()).thenReturn(userId);
    mockMvc
        .perform(delete(commentUriBuilder.buildAndExpand(nonExistentCommentId).toUri()))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.errorMessage").isNotEmpty());
  }
}
