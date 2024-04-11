package com.nickesqueda.socialmediademo.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

import com.nickesqueda.socialmediademo.dto.*;
import com.nickesqueda.socialmediademo.service.CommentService;
import com.nickesqueda.socialmediademo.service.PostService;
import com.nickesqueda.socialmediademo.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UsersController {

  private final UserService userService;
  private final PostService postService;
  private final CommentService commentService;

  @GetMapping("/{userId}")
  public UserResponseDto getUser(@PathVariable("userId") Long userId) {
    return userService.getUser(userId);
  }

  @PutMapping("/{userId}")
  public UserResponseDto updateUser(
      @PathVariable("userId") Long userId, @RequestBody @Valid UserRequestDto updatedUser) {
    return userService.updateUser(userId, updatedUser);
  }

  @GetMapping("/{userId}/posts")
  public List<PostResponseDto> getUsersPosts(@PathVariable("userId") Long userId) {
    return postService.getUsersPosts(userId);
  }

  @GetMapping("/{userId}/comments")
  public List<CommentResponseDto> getUsersComments(@PathVariable("userId") Long userId) {
    return commentService.getUsersComments(userId);
  }

  @PostMapping("/{userId}/posts")
  @ResponseStatus(CREATED)
  public PostResponseDto createPost(
      @PathVariable("userId") Long userId, @RequestBody @Valid PostRequestDto newPost) {
    return postService.createPost(userId, newPost);
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
