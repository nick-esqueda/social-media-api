package com.nickesqueda.socialmediademo.service;

import com.nickesqueda.socialmediademo.dto.PostDto;
import com.nickesqueda.socialmediademo.entity.Post;
import com.nickesqueda.socialmediademo.entity.UserEntity;
import com.nickesqueda.socialmediademo.exception.ResourceNotFoundException;
import com.nickesqueda.socialmediademo.exception.UnauthorizedOperationException;
import com.nickesqueda.socialmediademo.repository.PostRepository;
import com.nickesqueda.socialmediademo.repository.UserRepository;
import com.nickesqueda.socialmediademo.security.AuthUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostService {

  private final PostRepository postRepository;
  private final UserRepository userRepository;
  private final ModelMapper modelMapper;

  public PostDto getPost(Long postId) {
    Post postEntity = postRepository.retrieveOrElseThrow(postId);
    return modelMapper.map(postEntity, PostDto.class);
  }

  public List<PostDto> getUsersPosts(Long userId) {
    if (!userRepository.existsById(userId)) {
      throw new ResourceNotFoundException(UserEntity.class, userId);
    }

    List<Post> posts = postRepository.findByUserId(userId);
    return posts.stream().map(postEntity -> modelMapper.map(postEntity, PostDto.class)).toList();
  }

  public PostDto createPost(Long userId, PostDto postDto) {
    UserEntity userEntity = userRepository.retrieveOrElseThrow(userId);
    Long currentUserId = AuthUtils.getCurrentAuthenticatedUserId();

    if (!currentUserId.equals(userId)) {
      throw new UnauthorizedOperationException();
    }

    Post postEntity = modelMapper.map(postDto, Post.class);
    postEntity.setUser(userEntity);
    postEntity = postRepository.save(postEntity);
    return modelMapper.map(postEntity, PostDto.class);
  }

  public PostDto updatePost(Long postId, PostDto updatedPost) {
    Post postEntity = postRepository.retrieveOrElseThrow(postId);
    UserEntity userEntity = postEntity.getUser();
    Long currentUserId = AuthUtils.getCurrentAuthenticatedUserId();

    if (!currentUserId.equals(userEntity.getId())) {
      throw new UnauthorizedOperationException();
    }

    postEntity.setContent(updatedPost.getContent());
    postEntity = postRepository.save(postEntity);
    return modelMapper.map(postEntity, PostDto.class);
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
