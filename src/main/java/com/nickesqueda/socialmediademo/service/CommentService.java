package com.nickesqueda.socialmediademo.service;

import com.nickesqueda.socialmediademo.dto.CommentRequestDto;
import com.nickesqueda.socialmediademo.dto.CommentResponseDto;
import com.nickesqueda.socialmediademo.entity.Comment;
import com.nickesqueda.socialmediademo.entity.Post;
import com.nickesqueda.socialmediademo.entity.UserEntity;
import com.nickesqueda.socialmediademo.exception.ResourceNotFoundException;
import com.nickesqueda.socialmediademo.exception.UnauthorizedOperationException;
import com.nickesqueda.socialmediademo.repository.CommentRepository;
import com.nickesqueda.socialmediademo.repository.PostRepository;
import com.nickesqueda.socialmediademo.repository.UserRepository;
import com.nickesqueda.socialmediademo.security.AuthUtils;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@RequiredArgsConstructor
@Validated
@Service
public class CommentService {

  private final CommentRepository commentRepository;
  private final PostRepository postRepository;
  private final UserRepository userRepository;
  private final AuthUtils authUtils;
  private final ModelMapper modelMapper;

  public CommentResponseDto getComment(@NotNull Long commentId) {
    Comment commentEntity = commentRepository.retrieveOrElseThrow(commentId);
    return modelMapper.map(commentEntity, CommentResponseDto.class);
  }

  public List<CommentResponseDto> getPostsComments(@NotNull Long postId) {
    if (!postRepository.existsById(postId)) {
      throw new ResourceNotFoundException(Post.class, postId);
    }

    List<Comment> comments = commentRepository.findByPostId(postId);
    return comments.stream()
        .map(commentEntity -> modelMapper.map(commentEntity, CommentResponseDto.class))
        .toList();
  }

  public List<CommentResponseDto> getUsersComments(@NotNull Long userId) {
    if (!userRepository.existsById(userId)) {
      throw new ResourceNotFoundException(UserEntity.class, userId);
    }

    List<Comment> comments = commentRepository.findByUserId(userId);
    return comments.stream()
        .map(commentEntity -> modelMapper.map(commentEntity, CommentResponseDto.class))
        .toList();
  }

  public CommentResponseDto createComment(
      @NotNull Long postId, @NotNull CommentRequestDto commentRequestDto) {
    Post postEntity = postRepository.retrieveOrElseThrow(postId);
    Long currentUserId = authUtils.getCurrentAuthenticatedUserId();
    UserEntity currentUser = userRepository.retrieveOrElseThrow(currentUserId);

    Comment commentEntity = modelMapper.map(commentRequestDto, Comment.class);
    commentEntity.setUser(currentUser);
    commentEntity.setPost(postEntity);
    commentEntity = commentRepository.save(commentEntity);

    return modelMapper.map(commentEntity, CommentResponseDto.class);
  }

  public CommentResponseDto updateComment(
      @NotNull Long commentId, @NotNull CommentRequestDto updatedComment) {
    Comment commentEntity = commentRepository.retrieveOrElseThrow(commentId);
    UserEntity userEntity = commentEntity.getUser();
    Long currentUserId = authUtils.getCurrentAuthenticatedUserId();

    if (!currentUserId.equals(userEntity.getId())) {
      throw new UnauthorizedOperationException();
    }

    modelMapper.map(updatedComment, commentEntity);
    commentEntity = commentRepository.save(commentEntity);

    return modelMapper.map(commentEntity, CommentResponseDto.class);
  }

  public void deleteComment(@NotNull Long commentId) {
    Comment commentEntity = commentRepository.retrieveOrElseThrow(commentId);
    UserEntity userEntity = commentEntity.getUser();
    Long currentUserId = authUtils.getCurrentAuthenticatedUserId();

    if (!currentUserId.equals(userEntity.getId())) {
      throw new UnauthorizedOperationException();
    }

    commentRepository.deleteById(commentId);
  }

  @Transactional
  public void deletePostsComments(@NotNull Long postId) {
    Post postEntity = postRepository.retrieveOrElseThrow(postId);
    UserEntity userEntity = postEntity.getUser();
    Long currentUserId = authUtils.getCurrentAuthenticatedUserId();

    if (!currentUserId.equals(userEntity.getId())) {
      throw new UnauthorizedOperationException();
    }

    commentRepository.deleteByPostId(postId);
  }

  @Transactional
  public void deleteUsersComments(@NotNull Long userId) {
    UserEntity userEntity = userRepository.retrieveOrElseThrow(userId);
    Long currentUserId = authUtils.getCurrentAuthenticatedUserId();

    if (!currentUserId.equals(userEntity.getId())) {
      throw new UnauthorizedOperationException();
    }

    commentRepository.deleteByUserId(userId);
  }
}
