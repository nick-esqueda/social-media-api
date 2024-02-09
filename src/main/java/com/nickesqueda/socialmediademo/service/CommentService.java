package com.nickesqueda.socialmediademo.service;

import com.nickesqueda.socialmediademo.entity.Comment;
import com.nickesqueda.socialmediademo.entity.Post;
import com.nickesqueda.socialmediademo.repository.CommentRepository;
import com.nickesqueda.socialmediademo.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
  private final CommentRepository commentRepository;
  private final PostRepository postRepository;

  public CommentService(CommentRepository commentRepository, PostRepository postRepository) {
    this.commentRepository = commentRepository;
    this.postRepository = postRepository;
  }

  public void createComment(int postId, Comment comment) {
    Optional<Post> postOptional = postRepository.findById(postId);
    Post post =
        postOptional.orElseThrow(
            () ->
                new EntityNotFoundException(
                    "Cannot create comment - parent postId " + postId + " not found."));

    comment.setPost(post);
    commentRepository.save(comment);
  }

  public Comment getComment(int id) {
    Optional<Comment> commentOptional = commentRepository.findById(id);
    return commentOptional.orElseThrow(EntityNotFoundException::new);
  }

  public List<Comment> getPostsComments(int postId) {
    return commentRepository.findByPostId(postId);
  }

  public List<Comment> getUsersComments(int userId) {
    return commentRepository.findByUserId(userId);
  }

  public Comment updateComment(int commentId, Comment updatedComment) {
    Optional<Comment> currentCommentOptional = commentRepository.findById(commentId);
    Comment currentComment =
        currentCommentOptional.orElseThrow(
            () ->
                new EntityNotFoundException(
                    "Cannot update comment - commentId " + commentId + " not found."));

    currentComment.setContent(updatedComment.getContent());
    commentRepository.save(currentComment);
    return currentComment;
  }

  public void deleteComment(int commentId) {
    commentRepository.deleteById(commentId);
  }
}
