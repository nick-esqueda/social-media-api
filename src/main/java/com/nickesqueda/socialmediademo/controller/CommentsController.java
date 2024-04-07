package com.nickesqueda.socialmediademo.controller;

import static org.springframework.http.HttpStatus.NO_CONTENT;

import com.nickesqueda.socialmediademo.dto.CommentRequestDto;
import com.nickesqueda.socialmediademo.dto.CommentResponseDto;
import com.nickesqueda.socialmediademo.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/comments")
public class CommentsController {

  private final CommentService commentService;

  @GetMapping("/{commentId}")
  public CommentResponseDto getComment(@PathVariable("commentId") Long commentId) {
    return commentService.getComment(commentId);
  }

  @PutMapping("/{commentId}")
  public CommentResponseDto updateComment(
      @PathVariable("commentId") Long commentId,
      @RequestBody @Valid CommentRequestDto updatedComment) {
    return commentService.updateComment(commentId, updatedComment);
  }

  @DeleteMapping("/{commentId}")
  @ResponseStatus(NO_CONTENT)
  public void deleteComment(@PathVariable("commentId") Long commentId) {
    commentService.deleteComment(commentId);
  }
}
