package com.nickesqueda.socialmediademo.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

import com.nickesqueda.socialmediademo.dto.CommentDto;
import com.nickesqueda.socialmediademo.dto.PostDto;
import com.nickesqueda.socialmediademo.service.CommentService;
import com.nickesqueda.socialmediademo.service.PostService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
public class PostsController {
  private final PostService postService;
  private final CommentService commentService;

  public PostsController(PostService postService, CommentService commentService) {
    this.postService = postService;
    this.commentService = commentService;
  }

  @GetMapping("/{postId}")
  public PostDto getPost(@PathVariable("postId") Long postId) {
    return postService.getPost(postId);
  }

  @GetMapping("/{postId}/comments")
  public List<CommentDto> getPostsComments(@PathVariable("postId") Long postId) {
    return commentService.getPostsComments(postId);
  }

  @PostMapping("/{postId}/comments")
  @ResponseStatus(CREATED)
  public CommentDto createComment(
      @PathVariable("postId") Long postId, @RequestBody @Valid CommentDto comment) {
    return commentService.createComment(postId, comment);
  }

  @PutMapping("/{postId}")
  public PostDto updatePost(
      @PathVariable("postId") Long postId, @RequestBody @Valid PostDto updatedPost) {
    return postService.updatePost(postId, updatedPost);
  }

  @DeleteMapping("/{postId}")
  @ResponseStatus(NO_CONTENT)
  public void deletePost(@PathVariable("postId") Long postId) {
    postService.deletePost(postId);
  }

  @DeleteMapping("/{postId}/comments")
  @ResponseStatus(NO_CONTENT)
  public void deletePostsComments(@PathVariable("postId") Long postId) {
    commentService.deletePostsComments(postId);
  }
}
