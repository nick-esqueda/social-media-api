package com.nickesqueda.socialmediademo.service;

import com.nickesqueda.socialmediademo.dto.PostRequestDto;
import com.nickesqueda.socialmediademo.dto.PostResponseDto;
import com.nickesqueda.socialmediademo.entity.Post;
import com.nickesqueda.socialmediademo.entity.UserEntity;
import com.nickesqueda.socialmediademo.exception.ResourceNotFoundException;
import com.nickesqueda.socialmediademo.exception.UnauthorizedOperationException;
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
public class PostService {

  private final PostRepository postRepository;
  private final UserRepository userRepository;
  private final ModelMapper modelMapper;

  public PostResponseDto getPost(@NotNull Long postId) {
    Post postEntity = postRepository.retrieveOrElseThrow(postId);
    return modelMapper.map(postEntity, PostResponseDto.class);
  }

  public List<PostResponseDto> getUsersPosts(@NotNull Long userId) {
    if (!userRepository.existsById(userId)) {
      throw new ResourceNotFoundException(UserEntity.class, userId);
    }

    List<Post> posts = postRepository.findByUserId(userId);
    return posts.stream()
        .map(postEntity -> modelMapper.map(postEntity, PostResponseDto.class))
        .toList();
  }

  public PostResponseDto createPost(@NotNull Long userId, @NotNull PostRequestDto newPost) {
    UserEntity userEntity = userRepository.retrieveOrElseThrow(userId);
    Long currentUserId = AuthUtils.getCurrentAuthenticatedUserId();

    if (!currentUserId.equals(userId)) {
      throw new UnauthorizedOperationException();
    }

    Post postEntity = modelMapper.map(newPost, Post.class);
    postEntity.setUser(userEntity);
    postEntity = postRepository.save(postEntity);
    return modelMapper.map(postEntity, PostResponseDto.class);
  }

  public PostResponseDto updatePost(@NotNull Long postId, @NotNull PostRequestDto updatedPost) {
    Post postEntity = postRepository.retrieveOrElseThrow(postId);
    UserEntity userEntity = postEntity.getUser();
    Long currentUserId = AuthUtils.getCurrentAuthenticatedUserId();

    if (!currentUserId.equals(userEntity.getId())) {
      throw new UnauthorizedOperationException();
    }

    modelMapper.map(updatedPost, postEntity);
    postEntity = postRepository.save(postEntity);

    return modelMapper.map(postEntity, PostResponseDto.class);
  }

  public void deletePost(@NotNull Long postId) {
    Post postEntity = postRepository.retrieveOrElseThrow(postId);
    UserEntity userEntity = postEntity.getUser();
    Long currentUserId = AuthUtils.getCurrentAuthenticatedUserId();

    if (!currentUserId.equals(userEntity.getId())) {
      throw new UnauthorizedOperationException();
    }

    postRepository.deleteById(postId);
  }

  @Transactional
  public void deleteUsersPosts(@NotNull Long userId) {
    UserEntity userEntity = userRepository.retrieveOrElseThrow(userId);
    Long currentUserId = AuthUtils.getCurrentAuthenticatedUserId();

    if (!currentUserId.equals(userEntity.getId())) {
      throw new UnauthorizedOperationException();
    }

    postRepository.deleteByUserId(userId);
  }
}
