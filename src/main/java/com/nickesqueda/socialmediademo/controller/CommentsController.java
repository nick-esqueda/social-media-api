package com.nickesqueda.socialmediademo.controller;

import com.nickesqueda.socialmediademo.dto.CommentDto;
import com.nickesqueda.socialmediademo.service.CommentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
public class CommentsController {
  private final CommentService commentService;

  public CommentsController(CommentService commentService) {
    this.commentService = commentService;
  }

  @GetMapping("/{commentId}")
  public CommentDto getComment(@PathVariable("commentId") Long commentId) {
    return commentService.getComment(commentId);
  }

  @PutMapping("/{commentId}")
  public CommentDto updateComment(@PathVariable("commentId") Long commentId, @RequestBody CommentDto updatedComment) {
    return commentService.updateComment(commentId, updatedComment);
  }

  @DeleteMapping("/{commentId}")
  public void deleteComment(@PathVariable("commentId") Long commentId) {
    commentService.deleteComment(commentId);
  }
}
