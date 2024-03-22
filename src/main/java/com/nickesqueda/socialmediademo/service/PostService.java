package com.nickesqueda.socialmediademo.service;

import com.nickesqueda.socialmediademo.dto.PostDto;
import com.nickesqueda.socialmediademo.entity.Post;
import com.nickesqueda.socialmediademo.entity.UserEntity;
import com.nickesqueda.socialmediademo.exception.ResourceNotFoundException;
import com.nickesqueda.socialmediademo.exception.UnauthorizedOperationException;
import com.nickesqueda.socialmediademo.mapper.PostMapper;
import com.nickesqueda.socialmediademo.repository.PostRepository;
import com.nickesqueda.socialmediademo.repository.UserRepository;
import com.nickesqueda.socialmediademo.security.AuthUtils;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostService {
  private final PostRepository postRepository;
  private final UserRepository userRepository;

  public PostService(PostRepository postRepository, UserRepository userRepository) {
    this.postRepository = postRepository;
    this.userRepository = userRepository;
  }

  public PostDto getPost(Long postId) {
    Post postEntity = postRepository.retrieveOrElseThrow(postId);
    return PostMapper.toDto(postEntity);
  }

  public List<PostDto> getUsersPosts(Long userId) {
    if (!userRepository.existsById(userId)) {
      throw new ResourceNotFoundException(UserEntity.class, userId);
    }

    List<Post> posts = postRepository.findByUserId(userId);
    return posts.stream().map(PostMapper::toDto).toList();
  }

  public PostDto createPost(Long userId, PostDto postDto) {
    UserEntity userEntity = userRepository.retrieveOrElseThrow(userId);
    Long currentUserId = AuthUtils.getCurrentAuthenticatedUserId();

    if (!currentUserId.equals(userId)) {
      throw new UnauthorizedOperationException();
    }

    Post postEntity = PostMapper.toEntity(postDto);
    postEntity.setUser(userEntity);
    postRepository.save(postEntity);
    return PostMapper.toDto(postEntity);
  }

  public PostDto updatePost(Long postId, PostDto updatedPost) {
    Post postEntity = postRepository.retrieveOrElseThrow(postId);
    UserEntity userEntity = postEntity.getUser();
    Long currentUserId = AuthUtils.getCurrentAuthenticatedUserId();

    if (!currentUserId.equals(userEntity.getId())) {
      throw new UnauthorizedOperationException();
    }

    postEntity.setContent(updatedPost.getContent());
    postRepository.save(postEntity);
    return PostMapper.toDto(postEntity);
  }

  public void deletePost(Long postId) {
    Post postEntity = postRepository.retrieveOrElseThrow(postId);
    UserEntity userEntity = postEntity.getUser();
    Long currentUserId = AuthUtils.getCurrentAuthenticatedUserId();

    if (!currentUserId.equals(userEntity.getId())) {
      throw new UnauthorizedOperationException();
    }

    postRepository.deleteById(postId);
  }

  @Transactional
  public void deleteUsersPosts(Long userId) {
    UserEntity userEntity = userRepository.retrieveOrElseThrow(userId);
    Long currentUserId = AuthUtils.getCurrentAuthenticatedUserId();

    if (!currentUserId.equals(userEntity.getId())) {
      throw new UnauthorizedOperationException();
    }

    postRepository.deleteByUserId(userId);
  }
}
