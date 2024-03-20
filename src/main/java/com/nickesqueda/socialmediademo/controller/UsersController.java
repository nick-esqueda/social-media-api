package com.nickesqueda.socialmediademo.controller;

import com.nickesqueda.socialmediademo.dto.CommentDto;
import com.nickesqueda.socialmediademo.dto.PostDto;
import com.nickesqueda.socialmediademo.service.CommentService;
import com.nickesqueda.socialmediademo.service.PostService;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UsersController {
  private final PostService postService;
  private final CommentService commentService;

  public UsersController(PostService postService, CommentService commentService) {
    this.postService = postService;
    this.commentService = commentService;
  }

  @GetMapping("/{userId}/posts")
  public List<PostDto> getUsersPosts(@PathVariable("userId") Long userId) {
    return postService.getUsersPosts(userId);
  }

  @GetMapping("/{userId}/comments")
  public List<CommentDto> getUsersComments(@PathVariable("userId") Long userId) {
    return commentService.getUsersComments(userId);
  }

  @PostMapping("/{userId}/posts")
  public void createPost(@PathVariable("userId") Long userId, @RequestBody PostDto postDto) {
    // TODO: return 201 CREATED.
    postService.createPost(userId, postDto);
  }

  @DeleteMapping("/{userId}/posts")
  public void deleteUsersPosts(@PathVariable("userId") Long userId) {
    postService.deleteUsersPosts(userId);
  }

  @DeleteMapping("/{userId}/comments")
  public void deleteUsersComments(@PathVariable("userId") Long userId) {
    commentService.deleteUsersComments(userId);
  }
}
