package com.nickesqueda.socialmediademo.service;

import com.nickesqueda.socialmediademo.entity.Comment;
import com.nickesqueda.socialmediademo.entity.Post;
import com.nickesqueda.socialmediademo.entity.UserEntity;
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

  public void createPost(int userId, Post post) {
    Optional<UserEntity> userOptional = userRepository.findById(userId);
    UserEntity user =
        userOptional.orElseThrow(
            () ->
                new EntityNotFoundException(
                    "Cannot create post - parent userId " + userId + " not found."));

    post.setUser(user);
    postRepository.save(post);
  }

  public Post getPost(int id) {
    Optional<Post> postOptional = postRepository.findById(id);
    return postOptional.orElseThrow(EntityNotFoundException::new);
  }

  public Post updatePost(int postId, Post updatedPost) {
    // TODO: optimize - remove additional find query.
    Optional<Post> currentPostOptional = postRepository.findById(postId);
    Post currentPost =
        currentPostOptional.orElseThrow(
            () ->
                new EntityNotFoundException(
                    "Cannot update post - postId " + postId + " not found."));

    currentPost.setContent(updatedPost.getContent());
    postRepository.save(currentPost); // TODO: is .save() needed?
    return currentPost;
  }

  public void deletePost(int postId) {
    postRepository.deleteById(postId);
  }

  public List<Post> getUsersPosts(int userId) {
    return postRepository.findByUserId(userId);
  }

  public void deleteUsersPosts(int userId) {
    postRepository.deleteByUserId(userId);
  }
}
