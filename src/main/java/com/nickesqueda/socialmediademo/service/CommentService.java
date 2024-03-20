package com.nickesqueda.socialmediademo.service;

import com.nickesqueda.socialmediademo.dto.CommentDto;
import com.nickesqueda.socialmediademo.entity.Comment;
import com.nickesqueda.socialmediademo.entity.Post;
import com.nickesqueda.socialmediademo.entity.UserEntity;
import com.nickesqueda.socialmediademo.exception.ResourceNotFoundException;
import com.nickesqueda.socialmediademo.exception.UnauthorizedOperationException;
import com.nickesqueda.socialmediademo.mapper.CommentMapper;
import com.nickesqueda.socialmediademo.repository.CommentRepository;
import com.nickesqueda.socialmediademo.repository.PostRepository;
import com.nickesqueda.socialmediademo.repository.UserRepository;
import com.nickesqueda.socialmediademo.security.AuthUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentService {
  private final CommentRepository commentRepository;
  private final PostRepository postRepository;
  private final UserRepository userRepository;
  private final AuthUtils authUtils;

  public CommentService(
      CommentRepository commentRepository,
      PostRepository postRepository,
      AuthUtils authUtils,
      UserRepository userRepository) {
    this.commentRepository = commentRepository;
    this.postRepository = postRepository;
    this.userRepository = userRepository;
    this.authUtils = authUtils;
  }

  public void createComment(int postId, CommentDto commentDto) {
    Post postEntity = postRepository.retrieveOrElseThrow(postId);
    UserEntity currentUser = authUtils.getCurrentAuthenticatedUser();

    Comment commentEntity = CommentMapper.toEntity(commentDto);
    commentEntity.setPost(postEntity);
    commentEntity.setUser(currentUser);
    commentRepository.save(commentEntity);
  }

  public CommentDto getComment(int commentId) {
    Comment commentEntity = commentRepository.retrieveOrElseThrow(commentId);
    return CommentMapper.toDto(commentEntity);
  }

  public List<CommentDto> getPostsComments(int postId) {
    if (!postRepository.existsById(postId)) {
      throw new ResourceNotFoundException(Post.class, postId);
    }
    List<Comment> comments = commentRepository.findByPostId(postId);
    return comments.stream().map(CommentMapper::toDto).toList();
  }

  public List<CommentDto> getUsersComments(int userId) {
    if (!userRepository.existsById(userId)) {
      throw new ResourceNotFoundException(UserEntity.class, userId);
    }
    List<Comment> comments = commentRepository.findByUserId(userId);
    return comments.stream().map(CommentMapper::toDto).toList();
  }

  public CommentDto updateComment(int commentId, CommentDto updatedComment) {
    Comment commentEntity = commentRepository.retrieveOrElseThrow(commentId);
    UserEntity currentUser = authUtils.getCurrentAuthenticatedUser();

    if (currentUser.equals(commentEntity.getUser())) {
      commentEntity.setContent(updatedComment.getContent());
      commentRepository.save(commentEntity);
      return CommentMapper.toDto(commentEntity);
    } else {
      throw new UnauthorizedOperationException("User is not authorized to perform this operation");
    }
  }

  public void deleteComment(int commentId) {
    Comment commentEntity = commentRepository.retrieveOrElseThrow(commentId);
    UserEntity currentUser = authUtils.getCurrentAuthenticatedUser();

    if (currentUser.equals(commentEntity.getUser())) {
      commentRepository.deleteById(commentId);
    } else {
      throw new UnauthorizedOperationException("User is not authorized to perform this operation");
    }
  }

  @Transactional
  public void deletePostsComments(int postId) {
    Post postEntity = postRepository.retrieveOrElseThrow(postId);
    UserEntity currentUser = authUtils.getCurrentAuthenticatedUser();

    if (currentUser.equals(postEntity.getUser())) {
      commentRepository.deleteByPostId(postId);
    } else {
      throw new UnauthorizedOperationException("User is not authorized to perform this operation");
    }
  }

  @Transactional
  public void deleteUsersComments(int userId) {
    UserEntity userEntity = userRepository.retrieveOrElseThrow(userId);
    UserEntity currentUser = authUtils.getCurrentAuthenticatedUser();

    if (currentUser.equals(userEntity)) {
      commentRepository.deleteByUserId(userId);
    } else {
      throw new UnauthorizedOperationException("User is not authorized to perform this operation");
    }
  }
}
