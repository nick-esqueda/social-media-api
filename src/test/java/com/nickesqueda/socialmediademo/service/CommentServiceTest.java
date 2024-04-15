package com.nickesqueda.socialmediademo.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.nickesqueda.socialmediademo.config.ServiceLayerTestContextConfig;
import com.nickesqueda.socialmediademo.dto.CommentRequestDto;
import com.nickesqueda.socialmediademo.dto.CommentResponseDto;
import com.nickesqueda.socialmediademo.entity.Comment;
import com.nickesqueda.socialmediademo.entity.Post;
import com.nickesqueda.socialmediademo.entity.UserEntity;
import com.nickesqueda.socialmediademo.exception.ResourceNotFoundException;
import com.nickesqueda.socialmediademo.repository.CommentRepository;
import com.nickesqueda.socialmediademo.repository.PostRepository;
import com.nickesqueda.socialmediademo.repository.UserRepository;
import com.nickesqueda.socialmediademo.security.AuthUtils;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
class CommentServiceTest {

  private static final String TEST_STRING = "TEST";
  private static final String TEST_STRING2 = "TEST2";

  @Autowired private CommentService commentService;
  @Autowired private CommentRepository commentRepository;
  @Autowired private PostRepository postRepository;
  @Autowired private UserRepository userRepository;
  @Autowired private AuthUtils authUtils;
  private Long testUserId;
  private Long testPostId;
  private Long testCommentId;
  private Long unauthorizedUserId;
  private UserEntity userEntityStub;
  private Post postEntityStub;
  private Comment commentEntityStub;
  private CommentResponseDto expectedCommentResponseDto;
  private CommentRequestDto createCommentRequestDto;
  private List<Comment> postsCommentsStub;
  private List<Comment> usersCommentsStub;
  private List<CommentResponseDto> expectedPostsCommentsList;
  private List<CommentResponseDto> expectedUsersCommentsList;

  @BeforeEach
  public void beforeEach() {
    testCommentId = 1L;
    testPostId = 1L;
    testUserId = 1L;
    unauthorizedUserId = 2L;
    userEntityStub = UserEntity.builder().id(testUserId).username(TEST_STRING).build();
    postEntityStub =
        Post.builder().id(testPostId).content(TEST_STRING).user(userEntityStub).build();
    commentEntityStub =
        Comment.builder()
            .id(testCommentId)
            .content(TEST_STRING)
            .user(userEntityStub)
            .post(postEntityStub)
            .build();
    expectedCommentResponseDto =
        CommentResponseDto.builder().id(testCommentId).content(TEST_STRING).build();
    createCommentRequestDto = CommentRequestDto.builder().content(TEST_STRING).build();

    Comment listComment1 = Comment.builder().id(2L).content(TEST_STRING).build();
    Comment listComment2 = Comment.builder().id(3L).content(TEST_STRING).build();
    postsCommentsStub = List.of(listComment1, listComment2);
    usersCommentsStub = List.of(listComment1, listComment2);
    CommentResponseDto listCommentResponseDto1 =
        CommentResponseDto.builder().id(2L).content(TEST_STRING).build();
    CommentResponseDto listCommentResponseDto2 =
        CommentResponseDto.builder().id(3L).content(TEST_STRING).build();
    expectedPostsCommentsList = List.of(listCommentResponseDto1, listCommentResponseDto2);
    expectedUsersCommentsList = List.of(listCommentResponseDto1, listCommentResponseDto2);
  }

  @Test
  void getComment_ShouldReturnComment_GivenValidId() {
    when(commentRepository.retrieveOrElseThrow(testCommentId)).thenReturn(commentEntityStub);

    CommentResponseDto result = commentService.getComment(testCommentId);

    assertThat(result).isEqualTo(expectedCommentResponseDto);
  }

  @Test
  void getComment_ShouldThrow_GivenNullId() {
    testCommentId = null;

    assertThatThrownBy(() -> commentService.getComment(testCommentId))
        .isNotInstanceOf(NullPointerException.class)
        .hasMessageContaining("must not be null");
  }

  @Test
  void getComment_ShouldThrow_GivenCommentDoesNotExist() {
    testCommentId = 10000L;
    when(commentRepository.retrieveOrElseThrow(testCommentId))
        .thenThrow(ResourceNotFoundException.class);

    assertThatThrownBy(() -> commentService.getComment(testCommentId))
        .isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  void getPostsComments_ShouldReturnListOfComments_GivenValidUserId() {
    when(postRepository.existsById(testUserId)).thenReturn(true);
    when(commentRepository.findByPostId(testPostId)).thenReturn(postsCommentsStub);

    List<CommentResponseDto> result = commentService.getPostsComments(testPostId);

    assertThat(result).isEqualTo(expectedPostsCommentsList);
  }

  @Test
  void getPostsComments_ShouldThrow_GivenNullUserId() {
    testPostId = null;

    assertThatThrownBy(() -> commentService.getPostsComments(testPostId))
        .isNotInstanceOf(NullPointerException.class)
        .hasMessageContaining("must not be null");
  }

  @Test
  void getPostsComments_ShouldThrow_GivenUserDoesNotExist() {
    testUserId = 10000L;
    when(postRepository.existsById(testPostId)).thenReturn(false);

    assertThatThrownBy(() -> commentService.getPostsComments(testPostId))
        .isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  void getUsersComments_ShouldReturnListOfComments_GivenValidUserId() {
    when(userRepository.existsById(testUserId)).thenReturn(true);
    when(commentRepository.findByUserId(testUserId)).thenReturn(usersCommentsStub);

    List<CommentResponseDto> result = commentService.getUsersComments(testUserId);

    assertThat(result).isEqualTo(expectedUsersCommentsList);
  }

  @Test
  void getUsersComments_ShouldThrow_GivenNullUserId() {
    testUserId = null;

    assertThatThrownBy(() -> commentService.getUsersComments(testUserId))
        .isNotInstanceOf(NullPointerException.class)
        .hasMessageContaining("must not be null");
  }

  @Test
  void getUsersComments_ShouldThrow_GivenUserDoesNotExist() {
    testUserId = 10000L;
    when(userRepository.existsById(testUserId)).thenReturn(false);

    assertThatThrownBy(() -> commentService.getUsersComments(testUserId))
        .isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  void createComment_ShouldReturnComment_GivenValidArgs() {
    when(postRepository.retrieveOrElseThrow(testPostId)).thenReturn(postEntityStub);
    when(authUtils.getCurrentAuthenticatedUserId()).thenReturn(testUserId);
    when(userRepository.retrieveOrElseThrow(testUserId)).thenReturn(userEntityStub);
    when(commentRepository.save(any(Comment.class))).thenReturn(commentEntityStub);

    CommentResponseDto result = commentService.createComment(testPostId, createCommentRequestDto);

    assertThat(result).isEqualTo(expectedCommentResponseDto);
  }

  @Test
  void createComment_ShouldThrow_GivenNullArgs() {
    assertThatThrownBy(() -> commentService.createComment(null, createCommentRequestDto))
        .isNotInstanceOf(NullPointerException.class)
        .hasMessageContaining("must not be null");

    assertThatThrownBy(() -> commentService.createComment(testPostId, null))
        .isNotInstanceOf(NullPointerException.class)
        .hasMessageContaining("must not be null");
  }

  @Test
  void createComment_ShouldThrow_GivenPostDoesNotExist() {
    testPostId = 10000L;
    when(postRepository.retrieveOrElseThrow(testPostId)).thenThrow(ResourceNotFoundException.class);

    assertThatThrownBy(() -> commentService.createComment(testPostId, createCommentRequestDto))
        .isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  void createComment_ShouldThrow_GivenUserDoesNotExist() {
    testUserId = 10000L;
    when(postRepository.retrieveOrElseThrow(testPostId)).thenReturn(postEntityStub);
    when(authUtils.getCurrentAuthenticatedUserId()).thenReturn(testUserId);
    when(userRepository.retrieveOrElseThrow(testUserId)).thenThrow(ResourceNotFoundException.class);

    assertThatThrownBy(() -> commentService.createComment(testPostId, createCommentRequestDto))
        .isInstanceOf(ResourceNotFoundException.class);
  }
}
