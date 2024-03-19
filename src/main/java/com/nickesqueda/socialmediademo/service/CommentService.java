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
import com.nickesqueda.socialmediademo.security.AuthUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentService {
  private final CommentRepository commentRepository;
  private final PostRepository postRepository;
  private final AuthUtils authUtils;

  public CommentService(
      CommentRepository commentRepository, PostRepository postRepository, AuthUtils authUtils) {
    this.commentRepository = commentRepository;
    this.postRepository = postRepository;
    this.authUtils = authUtils;
  }

  public void createComment(int postId, CommentDto commentDto) {
    Post postEntity =
        postRepository
            .findById(postId)
            .orElseThrow(() -> new ResourceNotFoundException(Post.class, postId));
    UserEntity currentUser = authUtils.getCurrentlyAuthenticatedUserEntity();

    Comment commentEntity = CommentMapper.toEntity(commentDto);
    commentEntity.setPost(postEntity);
    commentEntity.setUser(currentUser);
    commentRepository.save(commentEntity);
  }

  public CommentDto getComment(int commentId) {
    Comment commentEntity =
        commentRepository
            .findById(commentId)
            .orElseThrow(() -> new ResourceNotFoundException(Comment.class, commentId));
    return CommentMapper.toDto(commentEntity);
  }

  public List<CommentDto> getPostsComments(int postId) {
    // TODO: handle case where post doesn't exist? NOTE: JPA returns empty list here by design.
    List<Comment> comments = commentRepository.findByPostId(postId);
    return comments.stream().map(CommentMapper::toDto).toList();
  }

  public List<CommentDto> getUsersComments(int userId) {
    // TODO: handle case where user doesn't exist? NOTE: JPA returns empty list by design in this
    // scenario.
    List<Comment> comments = commentRepository.findByUserId(userId);
    return comments.stream().map(CommentMapper::toDto).toList();
  }

  public CommentDto updateComment(int commentId, CommentDto updatedComment) {
    Comment commentEntity =
        commentRepository
            .findById(commentId)
            .orElseThrow(() -> new ResourceNotFoundException(Comment.class, commentId));
    UserEntity currentUser = authUtils.getCurrentlyAuthenticatedUserEntity();

    if (currentUser.equals(commentEntity.getUser())) {
      commentEntity.setContent(updatedComment.getContent());
      commentRepository.save(commentEntity);
      return CommentMapper.toDto(commentEntity);
    } else {
      throw new UnauthorizedOperationException("User is not authorized to perform this operation");
    }
  }

  public void deleteComment(int commentId) {
    Comment commentEntity =
        commentRepository
            .findById(commentId)
            .orElseThrow(() -> new ResourceNotFoundException(Comment.class, commentId));
    UserEntity currentUser = authUtils.getCurrentlyAuthenticatedUserEntity();

    if (currentUser.equals(commentEntity.getUser())) {
      commentRepository.deleteById(commentId);
    } else {
      throw new UnauthorizedOperationException("User is not authorized to perform this operation");
    }
  }

  @Transactional
  public void deletePostsComments(int postId) {
    Post postEntity =
        postRepository
            .findById(postId)
            .orElseThrow(() -> new ResourceNotFoundException(Post.class, postId));
    UserEntity currentUser = authUtils.getCurrentlyAuthenticatedUserEntity();

    if (currentUser.equals(postEntity.getUser())) {
      commentRepository.deleteByPostId(postId);
    } else {
      throw new UnauthorizedOperationException("User is not authorized to perform this operation");
    }
  }

  @Transactional
  public void deleteUsersComments(int userId) {
    UserEntity currentUser = authUtils.getCurrentlyAuthenticatedUserEntity();
    if (currentUser.getId() == userId) {
      commentRepository.deleteByUserId(userId);
    } else {
      throw new UnauthorizedOperationException("User is not authorized to perform this operation");
    }
  }
}
