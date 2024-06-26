package com.nickesqueda.socialmediademo.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

import com.nickesqueda.socialmediademo.dto.CommentRequestDto;
import com.nickesqueda.socialmediademo.dto.CommentResponseDto;
import com.nickesqueda.socialmediademo.dto.PostRequestDto;
import com.nickesqueda.socialmediademo.dto.PostResponseDto;
import com.nickesqueda.socialmediademo.service.CommentService;
import com.nickesqueda.socialmediademo.service.PostService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/posts")
public class PostsController {

  private final PostService postService;
  private final CommentService commentService;

  @PostMapping
  @ResponseStatus(CREATED)
  public PostResponseDto createPost(@RequestBody @Valid PostRequestDto newPost) {
    return postService.createPost(newPost);
  }

  @GetMapping("/{postId}")
  public PostResponseDto getPost(@PathVariable("postId") Long postId) {
    return postService.getPost(postId);
  }

  @PutMapping("/{postId}")
  public PostResponseDto updatePost(
      @PathVariable("postId") Long postId, @RequestBody @Valid PostRequestDto updatedPost) {
    return postService.updatePost(postId, updatedPost);
  }

  @DeleteMapping("/{postId}")
  @ResponseStatus(NO_CONTENT)
  public void deletePost(@PathVariable("postId") Long postId) {
    postService.deletePost(postId);
  }

  @GetMapping("/{postId}/comments")
  public List<CommentResponseDto> getPostsComments(@PathVariable("postId") Long postId) {
    return commentService.getPostsComments(postId);
  }

  @PostMapping("/{postId}/comments")
  @ResponseStatus(CREATED)
  public CommentResponseDto createComment(
      @PathVariable("postId") Long postId, @RequestBody @Valid CommentRequestDto newComment) {
    return commentService.createComment(postId, newComment);
  }

  @DeleteMapping("/{postId}/comments")
  @ResponseStatus(NO_CONTENT)
  public void deletePostsComments(@PathVariable("postId") Long postId) {
    commentService.deletePostsComments(postId);
  }
}
