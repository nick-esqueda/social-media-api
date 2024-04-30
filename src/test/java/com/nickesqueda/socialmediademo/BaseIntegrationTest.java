package com.nickesqueda.socialmediademo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.nickesqueda.socialmediademo.security.AuthUtils;
import java.net.URI;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.*;
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
  @MockBean AuthUtils authUtils;
  static ObjectMapper objectMapper;
  static URI baseUri;
  static URI loginUrl;
  static URI user1Url;
  static URI user2Url;
  static URI nonExistentUserUrl;

  static {
    mySQLContainer = new MySQLContainer<>(DockerImageName.parse("mysql:5.7.34")).withReuse(true);
    mySQLContainer.start();
  }

  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
    dynamicPropertyRegistry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
    dynamicPropertyRegistry.add("spring.datasource.username", mySQLContainer::getUsername);
    dynamicPropertyRegistry.add("spring.datasource.password", mySQLContainer::getPassword);
  }

  @BeforeAll
  static void setUp() {
    // use .findAndAddModules() to enable java.time.Instant serialization.
    objectMapper = JsonMapper.builder().findAndAddModules().build();
    baseUri = UriComponentsBuilder.newInstance().path("/api/v1").build().toUri();
    loginUrl = UriComponentsBuilder.fromUri(baseUri).path("/auth/login").build().toUri();
    user1Url =
        UriComponentsBuilder.fromUri(baseUri).path("/users/{userId}").buildAndExpand(1).toUri();
    user2Url =
        UriComponentsBuilder.fromUri(baseUri).path("/users/{userId}").buildAndExpand(2).toUri();
    nonExistentUserUrl =
        UriComponentsBuilder.fromUri(baseUri).path("/users/{userId}").buildAndExpand(1000).toUri();
  }
}
