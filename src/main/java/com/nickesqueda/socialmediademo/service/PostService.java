package com.nickesqueda.socialmediademo.service;

import com.nickesqueda.socialmediademo.dto.PostDto;
import com.nickesqueda.socialmediademo.entity.Comment;
import com.nickesqueda.socialmediademo.entity.Post;
import com.nickesqueda.socialmediademo.entity.UserEntity;
import com.nickesqueda.socialmediademo.mapper.PostMapper;
import com.nickesqueda.socialmediademo.repository.PostRepository;
import com.nickesqueda.socialmediademo.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {
  private final PostRepository postRepository;
  private final UserRepository userRepository;

  public PostService(PostRepository postRepository, UserRepository userRepository) {
    this.postRepository = postRepository;
    this.userRepository = userRepository;
  }

  public void createPost(int userId, PostDto postDto) {
    Optional<UserEntity> userOptional = userRepository.findById(userId);
    UserEntity userEntity =
        userOptional.orElseThrow(
            () ->
                new EntityNotFoundException(
                    "Cannot create post - parent userId " + userId + " not found."));

    Post postEntity = PostMapper.toEntity(postDto);
    postEntity.setUser(userEntity);
    postRepository.save(postEntity);
  }

  public PostDto getPost(int id) {
    Optional<Post> postOptional = postRepository.findById(id);
    Post postEntity = postOptional.orElseThrow(EntityNotFoundException::new);
    return PostMapper.toDto(postEntity);
  }

  public PostDto updatePost(int postId, PostDto updatedPost) {
    // TODO: optimize - remove additional find query.
    Optional<Post> postEntityOptional = postRepository.findById(postId);
    Post postEntity =
        postEntityOptional.orElseThrow(
            () ->
                new EntityNotFoundException(
                    "Cannot update post - postId " + postId + " not found."));

    postEntity.setContent(updatedPost.getContent());
    postRepository.save(postEntity); // TODO: is .save() needed?
    return PostMapper.toDto(postEntity);
  }

  public void deletePost(int postId) {
    postRepository.deleteById(postId);
  }

  public List<PostDto> getUsersPosts(int userId) {
    List<Post> posts = postRepository.findByUserId(userId);
    return posts.stream().map(PostMapper::toDto).toList();
  }

  public void deleteUsersPosts(int userId) {
    postRepository.deleteByUserId(userId);
  }
}
