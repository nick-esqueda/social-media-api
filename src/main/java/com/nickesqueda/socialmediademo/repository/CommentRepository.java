package com.nickesqueda.socialmediademo.repository;

import com.nickesqueda.socialmediademo.entity.Comment;
import com.nickesqueda.socialmediademo.exception.ResourceNotFoundException;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
  List<Comment> findByPostId(Long postId);

  List<Comment> findByUserId(Long userId);

  void deleteByPostId(Long postId);

  void deleteByUserId(Long userId);

  default Comment retrieveOrElseThrow(Long commentId) {
    return findById(commentId)
        .orElseThrow(() -> new ResourceNotFoundException(Comment.class, commentId));
  }
}
