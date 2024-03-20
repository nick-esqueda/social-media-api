package com.nickesqueda.socialmediademo.controller;

import com.nickesqueda.socialmediademo.dto.CommentDto;
import com.nickesqueda.socialmediademo.dto.PostDto;
import com.nickesqueda.socialmediademo.service.CommentService;
import com.nickesqueda.socialmediademo.service.PostService;
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

  @PutMapping("/{postId}")
  public PostDto updatePost(@PathVariable("postId") Long postId, @RequestBody PostDto updatedPost) {
    return postService.updatePost(postId, updatedPost);
  }

  @DeleteMapping("/{postId}")
  public void deletePost(@PathVariable("postId") Long postId) {
    postService.deletePost(postId);
  }

  @GetMapping("/{postId}/comments")
  public List<CommentDto> getPostsComments(@PathVariable("postId") Long postId) {
    return commentService.getPostsComments(postId);
  }

  @DeleteMapping("/{postId}/comments")
  public void deletePostsComments(@PathVariable("postId") Long postId) {
    commentService.deletePostsComments(postId);
  }

  @PostMapping("/{postId}/comments")
  public void createComment(@PathVariable("postId") Long postId, @RequestBody CommentDto comment) {
    // TODO: return 201 CREATED.
    commentService.createComment(postId, comment);
  }
}
