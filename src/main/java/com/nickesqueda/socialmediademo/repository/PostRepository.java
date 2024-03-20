package com.nickesqueda.socialmediademo.repository;

import com.nickesqueda.socialmediademo.entity.Post;
import com.nickesqueda.socialmediademo.exception.ResourceNotFoundException;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
  List<Post> findByUserId(Long userId);

  void deleteById(Long postId);

  void deleteByUserId(Long userId);

  default Post retrieveOrElseThrow(Long postId) {
    return findById(postId).orElseThrow(() -> new ResourceNotFoundException(Post.class, postId));
  }
}
