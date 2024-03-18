package com.nickesqueda.socialmediademo.service;

import com.nickesqueda.socialmediademo.dto.CommentDto;
import com.nickesqueda.socialmediademo.entity.Comment;
import com.nickesqueda.socialmediademo.entity.Post;
import com.nickesqueda.socialmediademo.entity.UserEntity;
import com.nickesqueda.socialmediademo.mapper.CommentMapper;
import com.nickesqueda.socialmediademo.repository.CommentRepository;
import com.nickesqueda.socialmediademo.repository.PostRepository;
import com.nickesqueda.socialmediademo.repository.UserRepository;
import com.nickesqueda.socialmediademo.security.AuthUtils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
    Optional<Post> postOptional = postRepository.findById(postId);
    Post postEntity =
        postOptional.orElseThrow(
            () ->
                new EntityNotFoundException(
                    "Cannot create comment - parent postId " + postId + " not found."));
    UserEntity userEntity = authUtils.getCurrentUserEntity();

    Comment commentEntity = CommentMapper.toEntity(commentDto);
    commentEntity.setPost(postEntity);
    commentEntity.setUser(userEntity);
    commentRepository.save(commentEntity);
  }

  public CommentDto getComment(int id) {
    Optional<Comment> commentOptional = commentRepository.findById(id);
    Comment commentEntity = commentOptional.orElseThrow(EntityNotFoundException::new);
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
    Optional<Comment> commentEntityOptional = commentRepository.findById(commentId);
    Comment commentEntity =
        commentEntityOptional.orElseThrow(
            () ->
                new EntityNotFoundException(
                    "Cannot update comment - commentId " + commentId + " not found."));

    commentEntity.setContent(updatedComment.getContent());
    commentRepository.save(commentEntity);
    return CommentMapper.toDto(commentEntity);
  }

  public void deleteComment(int commentId) {
    commentRepository.deleteById(commentId);
  }

  @Transactional
  public void deletePostsComments(int postId) {
    commentRepository.deleteByPostId(postId);
  }

  @Transactional
  public void deleteUsersComments(int userId) {
    commentRepository.deleteByUserId(userId);
  }
}
