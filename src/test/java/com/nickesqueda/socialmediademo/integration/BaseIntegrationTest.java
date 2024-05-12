package com.nickesqueda.socialmediademo.integration;

import com.jayway.jsonpath.JsonPath;
import com.nickesqueda.socialmediademo.repository.CommentRepository;
import com.nickesqueda.socialmediademo.repository.PostRepository;
import com.nickesqueda.socialmediademo.repository.UserRepository;
import com.nickesqueda.socialmediademo.security.AuthUtils;
import jakarta.persistence.EntityManager;
import java.net.URI;
import java.time.Instant;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.UriComponentsBuilder;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class BaseIntegrationTest {

  static final MySQLContainer<?> mySQLContainer;
  @Autowired MockMvc mockMvc;
  @Autowired EntityManager entityManager;
  @Autowired UserRepository userRepository;
  @Autowired PostRepository postRepository;
  @Autowired CommentRepository commentRepository;
  @MockBean AuthUtils authUtils;
  static URI baseUri;
  static URI registerUserUri;
  static URI passwordLoginUri;
  static URI allPostsUri;
  static UriComponentsBuilder userUriBuilder;
  static UriComponentsBuilder usersPostsUriBuilder;
  static UriComponentsBuilder usersCommentsUriBuilder;
  static UriComponentsBuilder postUriBuilder;
  static UriComponentsBuilder postsCommentsUriBuilder;
  static UriComponentsBuilder commentUriBuilder;
  static Long userId;
  static Long nonExistentUserId;
  static Long unauthorizedUserId;
  static Long postId;
  static Long nonExistentPostId;
  static Long commentId;
  static Long nonExistentCommentId;

  static {
    mySQLContainer = new MySQLContainer<>(DockerImageName.parse("mysql:5.7.34")).withReuse(true);
    mySQLContainer.start();
  }

  @BeforeAll
  static void setUp() {
    userId = 1L;
    nonExistentUserId = 1000000L;
    unauthorizedUserId = 2L;
    postId = 1L;
    nonExistentPostId = 1000000L;
    commentId = 1L;
    nonExistentCommentId = 1000000L;

    baseUri = UriComponentsBuilder.newInstance().path("/api/v1").build().toUri();
    registerUserUri = UriComponentsBuilder.fromUri(baseUri).path("/auth/register").build().toUri();
    passwordLoginUri = UriComponentsBuilder.fromUri(baseUri).path("/auth/login").build().toUri();
    allPostsUri = UriComponentsBuilder.fromUri(baseUri).path("/posts").build().toUri();
    userUriBuilder = UriComponentsBuilder.fromUri(baseUri).path("/users/{userId}");
    usersPostsUriBuilder = UriComponentsBuilder.fromUri(baseUri).path("/users/{userId}/posts");
    usersCommentsUriBuilder =
        UriComponentsBuilder.fromUri(baseUri).path("/users/{userId}/comments");
    postUriBuilder = UriComponentsBuilder.fromUri(baseUri).path("/posts/{postId}");
    postsCommentsUriBuilder =
        UriComponentsBuilder.fromUri(baseUri).path("/posts/{postId}/comments");
    commentUriBuilder = UriComponentsBuilder.fromUri(baseUri).path("/comments/{postId}");
  }

  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
    dynamicPropertyRegistry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
    dynamicPropertyRegistry.add("spring.datasource.username", mySQLContainer::getUsername);
    dynamicPropertyRegistry.add("spring.datasource.password", mySQLContainer::getPassword);
  }

  Map<String, Instant> extractAuditDates(String jsonString) {
    Instant createdAt = Instant.parse(JsonPath.read(jsonString, "$.createdAt"));
    Instant updatedAt = Instant.parse(JsonPath.read(jsonString, "$.updatedAt"));
    return Map.of("createdAt", createdAt, "updatedAt", updatedAt);
  }
}
