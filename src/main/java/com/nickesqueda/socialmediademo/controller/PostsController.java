package com.nickesqueda.socialmediademo.controller;

import com.nickesqueda.socialmediademo.dto.CommentDto;
import com.nickesqueda.socialmediademo.dto.PostDto;
import com.nickesqueda.socialmediademo.service.CommentService;
import com.nickesqueda.socialmediademo.service.PostService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
  public PostDto getPost(@PathVariable("postId") int postId) {
    return postService.getPost(postId);
  }

  @PutMapping("/{postId}")
  public PostDto updatePost(@PathVariable("postId") int postId, @RequestBody PostDto updatedPost) {
    return postService.updatePost(postId, updatedPost);
  }

  @DeleteMapping("/{postId}")
  public void deletePost(@PathVariable("postId") int postId) {
    postService.deletePost(postId);
  }

  @GetMapping("/{postId}/comments")
  public List<CommentDto> getPostsComments(@PathVariable("postId") int postId) {
    return commentService.getPostsComments(postId);
  }

  @PostMapping("/{postId}/comments")
  public void createComment(@PathVariable("postId") int postId, @RequestBody CommentDto comment) {
    // TODO: return 201 CREATED.
    commentService.createComment(postId, comment);
  }
}
