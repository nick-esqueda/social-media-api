package com.nickesqueda.socialmediademo.repository;

import com.nickesqueda.socialmediademo.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {
  List<Post> findByUserId(int userId);
  void deleteById(int postId);
  void deleteByUserId(int userId);
}
