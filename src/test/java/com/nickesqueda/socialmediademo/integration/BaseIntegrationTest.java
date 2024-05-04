package com.nickesqueda.socialmediademo.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.jayway.jsonpath.JsonPath;
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
  @MockBean AuthUtils authUtils;
  static ObjectMapper objectMapper;
  static URI baseUri;
  static UriComponentsBuilder userUriBuilder;
  static UriComponentsBuilder usersPostsUriBuilder;
  static UriComponentsBuilder usersCommentsUriBuilder;
  static UriComponentsBuilder postUriBuilder;
  static UriComponentsBuilder postsCommentsUriBuilder;
  static Long userId;
  static Long nonExistentUserId;
  static Long unauthorizedUserId;
  static Long postId;
  static Long nonExistentPostId;

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

    // use .findAndAddModules() to enable java.time.Instant serialization.
    objectMapper = JsonMapper.builder().findAndAddModules().build();
    baseUri = UriComponentsBuilder.newInstance().path("/api/v1").build().toUri();
    userUriBuilder = UriComponentsBuilder.fromUri(baseUri).path("/users/{userId}");
    usersPostsUriBuilder = UriComponentsBuilder.fromUri(baseUri).path("/users/{userId}/posts");
    usersCommentsUriBuilder =
        UriComponentsBuilder.fromUri(baseUri).path("/users/{userId}/comments");
    postUriBuilder = UriComponentsBuilder.fromUri(baseUri).path("/posts/{postId}");
    postsCommentsUriBuilder =
        UriComponentsBuilder.fromUri(baseUri).path("/posts/{postId}/comments");
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
