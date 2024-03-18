package com.nickesqueda.socialmediademo.repository;

import com.nickesqueda.socialmediademo.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
  List<Comment> findByPostId(int postId);
  List<Comment> findByUserId(int userId);
  void deleteByPostId(int postId);
  void deleteByUserId(int userId);
}
