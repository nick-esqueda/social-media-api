package com.nickesqueda.socialmediademo.repository;

import com.nickesqueda.socialmediademo.entity.Post;
import com.nickesqueda.socialmediademo.exception.ResourceNotFoundException;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Integer> {
  List<Post> findByUserId(int userId);

  void deleteById(int postId);

  void deleteByUserId(int userId);

  default Post retrieveOrElseThrow(int postId) {
    return findById(postId).orElseThrow(() -> new ResourceNotFoundException(Post.class, postId));
  }
}
