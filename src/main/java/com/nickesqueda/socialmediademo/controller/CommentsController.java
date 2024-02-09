package com.nickesqueda.socialmediademo.controller;

import com.nickesqueda.socialmediademo.entity.Comment;
import com.nickesqueda.socialmediademo.service.CommentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
public class CommentsController {
  private final CommentService commentService;

  public CommentsController(CommentService commentService) {
    this.commentService = commentService;
  }

  // TODO: use DTO instead of Entity objects in controller params.

  @GetMapping("/{commentId}")
  public Comment getComment(@PathVariable("commentId") int commentId) {
    return commentService.getComment(commentId);
  }

  @PutMapping("/{commentId}")
  public Comment updateComment(@PathVariable("commentId") int commentId, @RequestBody Comment updatedComment) {
    return commentService.updateComment(commentId, updatedComment);
  }

  @DeleteMapping("/{commentId}")
  public void deleteComment(@PathVariable("commentId") int commentId) {
    commentService.deleteComment(commentId);
  }
}
