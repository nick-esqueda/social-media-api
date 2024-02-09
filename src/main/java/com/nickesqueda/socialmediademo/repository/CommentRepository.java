package com.nickesqueda.socialmediademo.repository;

import com.nickesqueda.socialmediademo.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
  List<Comment> findByPostId(int postId);
  List<Comment> findByUserId(int userId);
}
