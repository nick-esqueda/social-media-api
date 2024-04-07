package com.nickesqueda.socialmediademo.service;

import com.nickesqueda.socialmediademo.dto.CommentDto;
import com.nickesqueda.socialmediademo.entity.Comment;
import com.nickesqueda.socialmediademo.entity.Post;
import com.nickesqueda.socialmediademo.entity.UserEntity;
import com.nickesqueda.socialmediademo.exception.ResourceNotFoundException;
import com.nickesqueda.socialmediademo.exception.UnauthorizedOperationException;
import com.nickesqueda.socialmediademo.repository.CommentRepository;
import com.nickesqueda.socialmediademo.repository.PostRepository;
import com.nickesqueda.socialmediademo.repository.UserRepository;
import com.nickesqueda.socialmediademo.security.AuthUtils;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentService {
  private final CommentRepository commentRepository;
  private final PostRepository postRepository;
  private final UserRepository userRepository;
  private final ModelMapper modelMapper;

  public CommentService(
      CommentRepository commentRepository,
      PostRepository postRepository,
      UserRepository userRepository,
      ModelMapper modelMapper) {
    this.commentRepository = commentRepository;
    this.postRepository = postRepository;
    this.userRepository = userRepository;
    this.modelMapper = modelMapper;
  }

  public CommentDto getComment(Long commentId) {
    Comment commentEntity = commentRepository.retrieveOrElseThrow(commentId);
    return modelMapper.map(commentEntity, CommentDto.class);
  }

  public List<CommentDto> getPostsComments(Long postId) {
    if (!postRepository.existsById(postId)) {
      throw new ResourceNotFoundException(Post.class, postId);
    }

    List<Comment> comments = commentRepository.findByPostId(postId);
    return comments.stream()
        .map(commentEntity -> modelMapper.map(commentEntity, CommentDto.class))
        .toList();
  }

  public List<CommentDto> getUsersComments(Long userId) {
    if (!userRepository.existsById(userId)) {
      throw new ResourceNotFoundException(UserEntity.class, userId);
    }

    List<Comment> comments = commentRepository.findByUserId(userId);
    return comments.stream()
        .map(commentEntity -> modelMapper.map(commentEntity, CommentDto.class))
        .toList();
  }

  public CommentDto createComment(Long postId, CommentDto commentDto) {
    Post postEntity = postRepository.retrieveOrElseThrow(postId);
    Long currentUserId = AuthUtils.getCurrentAuthenticatedUserId();
    UserEntity currentUser = userRepository.retrieveOrElseThrow(currentUserId);

    Comment commentEntity = modelMapper.map(commentDto, Comment.class);
    commentEntity.setUser(currentUser);
    commentEntity.setPost(postEntity);
    commentEntity = commentRepository.save(commentEntity);

    return modelMapper.map(commentEntity, CommentDto.class);
  }

  public CommentDto updateComment(Long commentId, CommentDto updatedComment) {
    Comment commentEntity = commentRepository.retrieveOrElseThrow(commentId);
    UserEntity userEntity = commentEntity.getUser();
    Long currentUserId = AuthUtils.getCurrentAuthenticatedUserId();

    if (!currentUserId.equals(userEntity.getId())) {
      throw new UnauthorizedOperationException();
    }

    commentEntity.setContent(updatedComment.getContent());
    commentEntity = commentRepository.save(commentEntity);
    return modelMapper.map(commentEntity, CommentDto.class);
  }

  public void deleteComment(Long commentId) {
    Comment commentEntity = commentRepository.retrieveOrElseThrow(commentId);
    UserEntity userEntity = commentEntity.getUser();
    Long currentUserId = AuthUtils.getCurrentAuthenticatedUserId();

    if (!currentUserId.equals(userEntity.getId())) {
      throw new UnauthorizedOperationException();
    }

    commentRepository.deleteById(commentId);
  }

  @Transactional
  public void deletePostsComments(Long postId) {
    Post postEntity = postRepository.retrieveOrElseThrow(postId);
    UserEntity userEntity = postEntity.getUser();
    Long currentUserId = AuthUtils.getCurrentAuthenticatedUserId();

    if (!currentUserId.equals(userEntity.getId())) {
      throw new UnauthorizedOperationException();
    }

    commentRepository.deleteByPostId(postId);
  }

  @Transactional
  public void deleteUsersComments(Long userId) {
    UserEntity userEntity = userRepository.retrieveOrElseThrow(userId);
    Long currentUserId = AuthUtils.getCurrentAuthenticatedUserId();

    if (!currentUserId.equals(userEntity.getId())) {
      throw new UnauthorizedOperationException();
    }

    commentRepository.deleteByUserId(userId);
  }
}
