package com.nickesqueda.socialmediademo;

import com.nickesqueda.socialmediademo.dto.AuthCredentialsDto;
import com.nickesqueda.socialmediademo.dto.LoginResponseDto;
import java.net.URI;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.util.UriComponentsBuilder;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class BaseIntegrationTest {

  static final MySQLContainer<?> mySQLContainer;
  @Autowired TestRestTemplate restTemplate;
  @LocalServerPort private int port;
  URI baseUri;
  URI loginUrl;
  URI user1Url;
  URI user2Url;
  URI nonExistentUserUrl;

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

  @BeforeEach
  void setUp() {
    baseUri =
        UriComponentsBuilder.newInstance()
            .scheme("http")
            .host("localhost")
            .port(port)
            .path("/api/v1")
            .build()
            .toUri();
    loginUrl = UriComponentsBuilder.fromUri(baseUri).path("/auth/login").build().toUri();
    user1Url =
        UriComponentsBuilder.fromUri(baseUri).path("/users/{userId}").buildAndExpand(1).toUri();
    user2Url =
        UriComponentsBuilder.fromUri(baseUri).path("/users/{userId}").buildAndExpand(2).toUri();
    nonExistentUserUrl =
        UriComponentsBuilder.fromUri(baseUri).path("/users/{userId}").buildAndExpand(1000).toUri();
  }

  String getAuthToken(String username) {
    AuthCredentialsDto authCredentialsDto =
        AuthCredentialsDto.builder().username(username).password("password").build();
    LoginResponseDto loginResponseDto =
        restTemplate.postForObject(loginUrl, authCredentialsDto, LoginResponseDto.class);
    return loginResponseDto.getAuthToken();
  }

  <T> RequestEntity<T> createAuthenticatedRequest(
      String username, URI url, T body, HttpMethod httpMethod) {
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(getAuthToken(username));
    return new RequestEntity<>(body, headers, httpMethod, url);
  }
}
