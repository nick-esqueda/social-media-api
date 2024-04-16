package com.nickesqueda.socialmediademo.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.nickesqueda.socialmediademo.config.ServiceLayerTestContextConfig;
import com.nickesqueda.socialmediademo.dto.PostRequestDto;
import com.nickesqueda.socialmediademo.dto.PostResponseDto;
import com.nickesqueda.socialmediademo.entity.Post;
import com.nickesqueda.socialmediademo.entity.UserEntity;
import com.nickesqueda.socialmediademo.exception.ResourceNotFoundException;
import com.nickesqueda.socialmediademo.exception.UnauthorizedOperationException;
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
class PostServiceTest {

  private static final String TEST_STRING = "TEST";
  private static final String TEST_STRING2 = "TEST2";

  @Autowired private PostService postService;
  @Autowired private PostRepository postRepository;
  @Autowired private UserRepository userRepository;
  @Autowired private AuthUtils authUtils;
  private Long testPostId;
  private Long testUserId;
  private Long unauthorizedUserId;
  private UserEntity userEntityStub;
  private Post postEntityStub;
  private Post postEntityStubForUpdate;
  private PostRequestDto postRequestDto;
  private PostRequestDto postRequestDtoForUpdate;
  private PostResponseDto expectedPostResponseDto;
  private PostResponseDto expectedPostResponseDtoForUpdate;
  private List<Post> usersPostsStub;
  private List<PostResponseDto> expectedUsersPostsList;

  @BeforeEach
  public void beforeEach() {
    testPostId = 1L;
    testUserId = 1L;
    unauthorizedUserId = 2L;
    userEntityStub = UserEntity.builder().id(testUserId).username(TEST_STRING).build();
    postEntityStub =
        Post.builder().id(testPostId).content(TEST_STRING).user(userEntityStub).build();
    postEntityStubForUpdate = Post.builder().id(testPostId).content(TEST_STRING2).build();
    postRequestDto = PostRequestDto.builder().content(TEST_STRING).build();
    postRequestDtoForUpdate = PostRequestDto.builder().content(TEST_STRING2).build();
    expectedPostResponseDto = PostResponseDto.builder().id(testPostId).content(TEST_STRING).build();
    expectedPostResponseDtoForUpdate =
        PostResponseDto.builder().id(testPostId).content(TEST_STRING2).build();

    Post usersPost1 = Post.builder().id(2L).content(TEST_STRING).build();
    Post usersPost2 = Post.builder().id(3L).content(TEST_STRING).build();
    usersPostsStub = List.of(usersPost1, usersPost2);
    PostResponseDto usersPost1Dto = PostResponseDto.builder().id(2L).content(TEST_STRING).build();
    PostResponseDto usersPost2Dto = PostResponseDto.builder().id(3L).content(TEST_STRING).build();
    expectedUsersPostsList = List.of(usersPost1Dto, usersPost2Dto);
  }

  @Test
  void getPost_ShouldReturnPost_GivenValidId() {
    when(postRepository.retrieveOrElseThrow(testPostId)).thenReturn(postEntityStub);

    PostResponseDto result = postService.getPost(testPostId);

    assertThat(result).isEqualTo(expectedPostResponseDto);
  }

  @Test
  void getPost_ShouldThrow_GivenNullId() {
    testPostId = null;

    assertThatThrownBy(() -> postService.getPost(testPostId))
        .isNotInstanceOf(NullPointerException.class)
        .hasMessageContaining("must not be null");
  }

  @Test
  void getPost_ShouldThrow_GivenPostDoesNotExist() {
    testPostId = 10000L;
    when(postRepository.retrieveOrElseThrow(testPostId)).thenThrow(ResourceNotFoundException.class);

    assertThatThrownBy(() -> postService.getPost(testPostId))
        .isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  void getUsersPosts_ShouldReturnListOfPosts_GivenValidUserId() {
    when(userRepository.existsById(testUserId)).thenReturn(true);
    when(postRepository.findByUserId(testUserId)).thenReturn(usersPostsStub);

    List<PostResponseDto> result = postService.getUsersPosts(testUserId);

    assertThat(result).isEqualTo(expectedUsersPostsList);
  }

  @Test
  void getUsersPosts_ShouldThrow_GivenNullUserId() {
    testUserId = null;

    assertThatThrownBy(() -> postService.getUsersPosts(testUserId))
        .isNotInstanceOf(NullPointerException.class)
        .hasMessageContaining("must not be null");
  }

  @Test
  void getUsersPosts_ShouldThrow_GivenUserDoesNotExist() {
    testUserId = 10000L;
    when(userRepository.existsById(testUserId)).thenReturn(false);

    assertThatThrownBy(() -> postService.getUsersPosts(testUserId))
        .isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  void createPost_ShouldReturnPost_GivenValidArgs() {
    when(userRepository.retrieveOrElseThrow(testUserId)).thenReturn(userEntityStub);
    when(authUtils.getCurrentAuthenticatedUserId()).thenReturn(testUserId);
    when(postRepository.save(any(Post.class))).thenReturn(postEntityStub);

    PostResponseDto result = postService.createPost(testUserId, postRequestDto);

    assertThat(result).isEqualTo(expectedPostResponseDto);
  }

  @Test
  void createPost_ShouldThrow_GivenNullArgs() {
    assertThatThrownBy(() -> postService.createPost(null, postRequestDto))
        .isNotInstanceOf(NullPointerException.class)
        .hasMessageContaining("must not be null");

    assertThatThrownBy(() -> postService.createPost(testUserId, null))
        .isNotInstanceOf(NullPointerException.class)
        .hasMessageContaining("must not be null");
  }

  @Test
  void createPost_ShouldThrow_GivenUserDoesNotExist() {
    testUserId = 10000L;
    when(userRepository.retrieveOrElseThrow(testUserId)).thenThrow(ResourceNotFoundException.class);

    assertThatThrownBy(() -> postService.createPost(testUserId, postRequestDto))
        .isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  void createPost_ShouldThrow_GivenUnauthorizedUserId() {
    when(userRepository.retrieveOrElseThrow(testUserId)).thenReturn(userEntityStub);
    when(authUtils.getCurrentAuthenticatedUserId()).thenReturn(unauthorizedUserId);

    assertThatThrownBy(() -> postService.createPost(testUserId, postRequestDto))
        .isInstanceOf(UnauthorizedOperationException.class);
  }

  @Test
  void updatePost_ShouldReturnPost_GivenValidArgs() {
    when(postRepository.retrieveOrElseThrow(testPostId)).thenReturn(postEntityStub);
    when(authUtils.getCurrentAuthenticatedUserId()).thenReturn(testUserId);
    when(postRepository.save(postEntityStub)).thenReturn(postEntityStubForUpdate);

    PostResponseDto result = postService.updatePost(testPostId, postRequestDtoForUpdate);

    assertThat(result).isEqualTo(expectedPostResponseDtoForUpdate);
  }

  @Test
  void updatePost_ShouldThrow_GivenNullArgs() {
    assertThatThrownBy(() -> postService.updatePost(null, postRequestDtoForUpdate))
        .isNotInstanceOf(NullPointerException.class)
        .hasMessageContaining("must not be null");

    assertThatThrownBy(() -> postService.updatePost(testPostId, null))
        .isNotInstanceOf(NullPointerException.class)
        .hasMessageContaining("must not be null");
  }

  @Test
  void updatePost_ShouldThrow_GivenUnauthorizedUser() {
    when(postRepository.retrieveOrElseThrow(testPostId)).thenReturn(postEntityStub);
    when(authUtils.getCurrentAuthenticatedUserId()).thenReturn(unauthorizedUserId);

    assertThatThrownBy(() -> postService.updatePost(testPostId, postRequestDtoForUpdate))
        .isInstanceOf(UnauthorizedOperationException.class);
  }

  @Test
  void updatePost_ShouldThrow_GivenPostDoesNotExist() {
    testPostId = 10000L;
    when(postRepository.retrieveOrElseThrow(testPostId)).thenThrow(ResourceNotFoundException.class);

    assertThatThrownBy(() -> postService.updatePost(testPostId, postRequestDtoForUpdate))
        .isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  void deletePost_ShouldInvokeDelete_GivenValidId() {
    when(postRepository.retrieveOrElseThrow(testPostId)).thenReturn(postEntityStub);
    when(authUtils.getCurrentAuthenticatedUserId()).thenReturn(testUserId);

    postService.deletePost(testPostId);

    verify(postRepository, times(1)).deleteById(testPostId);
  }

  @Test
  void deletePost_ShouldThrow_GivenNullId() {
    testPostId = null;

    assertThatThrownBy(() -> postService.deletePost(testPostId))
        .isNotInstanceOf(NullPointerException.class)
        .hasMessageContaining("must not be null");
  }

  @Test
  void deletePost_ShouldThrow_GivenPostDoesNotExist() {
    testPostId = 10000L;
    when(postRepository.retrieveOrElseThrow(testPostId)).thenThrow(ResourceNotFoundException.class);

    assertThatThrownBy(() -> postService.deletePost(testPostId))
        .isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  void deletePost_ShouldThrow_GivenUnauthorizedUser() {
    when(postRepository.retrieveOrElseThrow(testPostId)).thenReturn(postEntityStub);
    when(authUtils.getCurrentAuthenticatedUserId()).thenReturn(unauthorizedUserId);

    assertThatThrownBy(() -> postService.deletePost(testPostId))
        .isInstanceOf(UnauthorizedOperationException.class);
  }

  @Test
  void deleteUsersPosts_ShouldInvokeDelete_GivenValidId() {
    when(userRepository.retrieveOrElseThrow(testUserId)).thenReturn(userEntityStub);
    when(authUtils.getCurrentAuthenticatedUserId()).thenReturn(testUserId);

    postService.deleteUsersPosts(testUserId);

    verify(postRepository, times(1)).deleteByUserId(testUserId);
  }

  @Test
  void deleteUsersPosts_ShouldThrow_GivenNullId() {
    testUserId = null;

    assertThatThrownBy(() -> postService.deleteUsersPosts(testUserId))
        .isNotInstanceOf(NullPointerException.class)
        .hasMessageContaining("must not be null");
  }

  @Test
  void deleteUsersPosts_ShouldThrow_GivenUserDoesNotExist() {
    testUserId = 10000L;
    when(userRepository.retrieveOrElseThrow(testUserId)).thenThrow(ResourceNotFoundException.class);

    assertThatThrownBy(() -> postService.deleteUsersPosts(testUserId))
        .isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  void deleteUsersPosts_ShouldThrow_GivenUnauthorizedUser() {
    when(userRepository.retrieveOrElseThrow(testUserId)).thenReturn(userEntityStub);
    when(authUtils.getCurrentAuthenticatedUserId()).thenReturn(unauthorizedUserId);

    assertThatThrownBy(() -> postService.deleteUsersPosts(testUserId))
        .isInstanceOf(UnauthorizedOperationException.class);
  }
}
