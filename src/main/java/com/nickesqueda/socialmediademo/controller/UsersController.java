package com.nickesqueda.socialmediademo.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

import com.nickesqueda.socialmediademo.dto.CommentDto;
import com.nickesqueda.socialmediademo.dto.LoginResponseDto;
import com.nickesqueda.socialmediademo.dto.PostDto;
import com.nickesqueda.socialmediademo.dto.UserDto;
import com.nickesqueda.socialmediademo.service.CommentService;
import com.nickesqueda.socialmediademo.service.PostService;
import com.nickesqueda.socialmediademo.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UsersController {
  private final UserService userService;
  private final PostService postService;
  private final CommentService commentService;

  public UsersController(
      UserService userService, PostService postService, CommentService commentService) {
    this.userService = userService;
    this.postService = postService;
    this.commentService = commentService;
  }

  @GetMapping("/{userId}")
  public UserDto getUser(@PathVariable("userId") Long userId) {
    return userService.getUser(userId);
  }

  @PutMapping("/{userId}")
  public UserDto updateUser(
      @PathVariable("userId") Long userId, @RequestBody @Valid UserDto updatedUser) {
    return userService.updateUser(userId, updatedUser);
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
  @ResponseStatus(CREATED)
  public PostDto createPost(
      @PathVariable("userId") Long userId, @RequestBody @Valid PostDto postDto) {
    return postService.createPost(userId, postDto);
  }

  @DeleteMapping("/{userId}/posts")
  @ResponseStatus(NO_CONTENT)
  public void deleteUsersPosts(@PathVariable("userId") Long userId) {
    postService.deleteUsersPosts(userId);
  }

  @DeleteMapping("/{userId}/comments")
  @ResponseStatus(NO_CONTENT)
  public void deleteUsersComments(@PathVariable("userId") Long userId) {
    commentService.deleteUsersComments(userId);
  }
}
