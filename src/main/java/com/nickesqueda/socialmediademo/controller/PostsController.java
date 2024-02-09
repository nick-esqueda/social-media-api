package com.nickesqueda.socialmediademo.controller;

import com.nickesqueda.socialmediademo.entity.Comment;
import com.nickesqueda.socialmediademo.entity.Post;
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

  // TODO: use DTO instead of Entity objects in controller params.

  @GetMapping("/{postId}")
  public Post getPost(@PathVariable("postId") int postId) {
    return postService.getPost(postId);
  }

  @PutMapping("/{postId}")
  public Post updatePost(@PathVariable("postId") int postId, @RequestBody Post updatedPost) {
    return postService.updatePost(postId, updatedPost);
  }

  @DeleteMapping("/{postId}")
  public void deletePost(@PathVariable("postId") int postId) {
    postService.deletePost(postId);
  }

  @GetMapping("/{postId}/comments")
  public List<Comment> getPostsComments(@PathVariable("postId") int postId) {
    return commentService.getPostsComments(postId);
  }

  @PostMapping("/{postId}/comments")
  public void createComment(@PathVariable("postId") int postId, @RequestBody Comment comment) {
    commentService.createComment(postId, comment);
  }

}
