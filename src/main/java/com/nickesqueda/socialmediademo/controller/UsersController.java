package com.nickesqueda.socialmediademo.controller;

import com.nickesqueda.socialmediademo.entity.Comment;
import com.nickesqueda.socialmediademo.entity.Post;
import com.nickesqueda.socialmediademo.entity.UserEntity;
import com.nickesqueda.socialmediademo.service.CommentService;
import com.nickesqueda.socialmediademo.service.PostService;
import com.nickesqueda.socialmediademo.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UsersController {
  private final PostService postService;
  private final CommentService commentService;

  public UsersController(PostService postService, CommentService commentService) {
    this.postService = postService;
    this.commentService = commentService;
  }

  // TODO: use DTO instead of Entity objects in controller params.

  @GetMapping("/{userId}/posts")
  public List<Post> getUsersPosts(@PathVariable("userId") int userId) {
    return postService.getUsersPosts(userId);
  }

  @PostMapping("/{userId}/posts")
  public void createPost(@PathVariable("userId") int userId, @RequestBody Post post) {
    postService.createPost(userId, post);
  }

  @DeleteMapping("/{userId}/posts")
  public void deleteUsersPosts(@PathVariable("userId") int userId) {
    postService.deleteUsersPosts(userId);
  }

  @GetMapping("/{userId}/comments")
  public List<Comment> getUsersComments(@PathVariable("userId") int userId) {
    return commentService.getUsersComments(userId);
  }
}
