package com.nickesqueda.socialmediademo.controller;

import com.nickesqueda.socialmediademo.dto.CommentDto;
import com.nickesqueda.socialmediademo.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.NO_CONTENT;

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
  public CommentDto updateComment(
      @PathVariable("commentId") Long commentId, @RequestBody @Valid CommentDto updatedComment) {
    return commentService.updateComment(commentId, updatedComment);
  }

  @DeleteMapping("/{commentId}")
  @ResponseStatus(NO_CONTENT)
  public void deleteComment(@PathVariable("commentId") Long commentId) {
    commentService.deleteComment(commentId);
  }
}
