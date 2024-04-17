package com.nickesqueda.socialmediademo.test.config;

import com.nickesqueda.socialmediademo.repository.CommentRepository;
import com.nickesqueda.socialmediademo.repository.PostRepository;
import com.nickesqueda.socialmediademo.repository.UserRepository;
import com.nickesqueda.socialmediademo.security.AuthUtils;
import com.nickesqueda.socialmediademo.service.CommentService;
import com.nickesqueda.socialmediademo.service.PostService;
import com.nickesqueda.socialmediademo.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class ServiceLayerTestContextConfig {

  @MockBean private UserRepository userRepository;
  @MockBean private PostRepository postRepository;
  @MockBean private CommentRepository commentRepository;
  @MockBean private AuthUtils authUtils;

  @Bean
  public UserService userService(ModelMapper modelMapper) {
    return new UserService(userRepository, authUtils, modelMapper);
  }

  @Bean
  public PostService postService(ModelMapper modelMapper) {
    return new PostService(postRepository, userRepository, authUtils, modelMapper);
  }

  @Bean
  public CommentService commentService(ModelMapper modelMapper) {
    return new CommentService(
        commentRepository, postRepository, userRepository, authUtils, modelMapper);
  }

  @Bean
  public ModelMapper modelMapper() {
    return new ModelMapper();
  }
}
