package com.nickesqueda.socialmediademo.service;

import com.nickesqueda.socialmediademo.dto.PostDto;
import com.nickesqueda.socialmediademo.entity.Post;
import com.nickesqueda.socialmediademo.entity.UserEntity;
import com.nickesqueda.socialmediademo.exception.UnauthorizedOperationException;
import com.nickesqueda.socialmediademo.mapper.PostMapper;
import com.nickesqueda.socialmediademo.repository.PostRepository;
import com.nickesqueda.socialmediademo.repository.UserRepository;
import com.nickesqueda.socialmediademo.security.AuthUtils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {
  private final PostRepository postRepository;
  private final UserRepository userRepository;
  private final AuthUtils authUtils;

  public PostService(
      PostRepository postRepository, UserRepository userRepository, AuthUtils authUtils) {
    this.postRepository = postRepository;
    this.userRepository = userRepository;
    this.authUtils = authUtils;
  }

  public void createPost(int userId, PostDto postDto) {
    Optional<UserEntity> userOptional = userRepository.findById(userId);
    UserEntity userEntity =
        userOptional.orElseThrow(
            () ->
                new EntityNotFoundException(
                    "Cannot create post - parent userId " + userId + " not found."));
    UserEntity currentUser = authUtils.getCurrentlyAuthenticatedUserEntity();

    if (currentUser.equals(userEntity)) {
      Post postEntity = PostMapper.toEntity(postDto);
      postEntity.setUser(userEntity);
      postRepository.save(postEntity);
    } else {
      throw new UnauthorizedOperationException("User is not authorized to perform this operation");
    }
  }

  public PostDto getPost(int id) {
    Optional<Post> postOptional = postRepository.findById(id);
    Post postEntity = postOptional.orElseThrow(EntityNotFoundException::new);
    return PostMapper.toDto(postEntity);
  }

  public PostDto updatePost(int postId, PostDto updatedPost) {
    Optional<Post> postEntityOptional = postRepository.findById(postId);
    Post postEntity =
        postEntityOptional.orElseThrow(
            () ->
                new EntityNotFoundException(
                    "Cannot update post - postId " + postId + " not found."));
    UserEntity currentUser = authUtils.getCurrentlyAuthenticatedUserEntity();

    if (currentUser.equals(postEntity.getUser())) {
      postEntity.setContent(updatedPost.getContent());
      postRepository.save(postEntity);
      return PostMapper.toDto(postEntity);
    } else {
      throw new UnauthorizedOperationException("User is not authorized to perform this operation");
    }
  }

  public void deletePost(int postId) {
    Optional<Post> postOptional = postRepository.findById(postId);
    Post postEntity =
        postOptional.orElseThrow(
            () -> new EntityNotFoundException("postId " + postId + " not found."));
    UserEntity currentUser = authUtils.getCurrentlyAuthenticatedUserEntity();

    if (currentUser.equals(postEntity.getUser())) {
      postRepository.deleteById(postId);
    } else {
      throw new UnauthorizedOperationException("User is not authorized to perform this operation");
    }
  }

  public List<PostDto> getUsersPosts(int userId) {
    List<Post> posts = postRepository.findByUserId(userId);
    return posts.stream().map(PostMapper::toDto).toList();
  }

  @Transactional
  public void deleteUsersPosts(int userId) {
    UserEntity currentUser = authUtils.getCurrentlyAuthenticatedUserEntity();
    if (currentUser.getId() == userId) {
      postRepository.deleteByUserId(userId);
    } else {
      throw new UnauthorizedOperationException("User is not authorized to perform this operation");
    }
  }
}
