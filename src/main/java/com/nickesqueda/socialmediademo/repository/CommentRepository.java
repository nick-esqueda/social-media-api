package com.nickesqueda.socialmediademo.repository;

import com.nickesqueda.socialmediademo.entity.Comment;
import com.nickesqueda.socialmediademo.exception.ResourceNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
  List<Comment> findByPostId(int postId);

  List<Comment> findByUserId(int userId);

  void deleteByPostId(int postId);

  void deleteByUserId(int userId);

  default Comment retrieveOrElseThrow(int commentId) {
    return findById(commentId)
        .orElseThrow(() -> new ResourceNotFoundException(Comment.class, commentId));
  }
}
