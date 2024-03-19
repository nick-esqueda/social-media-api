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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    UserEntity userEntity =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException(UserEntity.class, userId));
    UserEntity currentUser = authUtils.getCurrentlyAuthenticatedUserEntity();

    if (currentUser.equals(userEntity)) {
      Post postEntity = PostMapper.toEntity(postDto);
      postEntity.setUser(userEntity);
      postRepository.save(postEntity);
    } else {
      throw new UnauthorizedOperationException("User is not authorized to perform this operation");
    }
  }

  public PostDto getPost(int postId) {
    Post postEntity =
        postRepository
            .findById(postId)
            .orElseThrow(() -> new ResourceNotFoundException(Post.class, postId));
    return PostMapper.toDto(postEntity);
  }

  public PostDto updatePost(int postId, PostDto updatedPost) {
    Post postEntity =
        postRepository
            .findById(postId)
            .orElseThrow(() -> new ResourceNotFoundException(Post.class, postId));
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
    Post postEntity =
        postRepository
            .findById(postId)
            .orElseThrow(() -> new ResourceNotFoundException(Post.class, postId));
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
