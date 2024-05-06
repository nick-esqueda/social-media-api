package com.nickesqueda.socialmediademo.integration;

import static com.nickesqueda.socialmediademo.test.util.TestConstants.*;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

public class AuthControllerIntegrationTest extends BaseIntegrationTest {

  @Test
  @Transactional
  void registerUser_ShouldReturnSuccessfulResponse_GivenValidCredentials() throws Exception {
    mockMvc
        .perform(
            post(registerUserUri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(testUserCredentialsJson))
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.authToken").isNotEmpty());
  }

  @Test
  @Transactional
  void registerUser_ShouldBeReflectedByGetUser_GivenSuccessfulRegistration() throws Exception {
    String response =
        mockMvc
            .perform(
                post(registerUserUri)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(testUserCredentialsJson))
            .andDo(print())
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();
    int registeredUserId = JsonPath.read(response, "$.id");

    mockMvc
        .perform(get(userUriBuilder.buildAndExpand(registeredUserId).toUri()))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value(TEST_USERNAME));
  }

  @Test
  @Transactional
  void registerUser_ShouldAllowUserToLogIn_GivenSuccessfulRegistration() throws Exception {
    mockMvc
        .perform(
            post(registerUserUri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(testUserCredentialsJson))
        .andDo(print())
        .andExpect(status().isCreated());

    mockMvc
        .perform(
            post(passwordLoginUri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(testUserCredentialsJson))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  @Transactional
  void registerUser_ShouldReturn400WithErrorMessage_GivenUsernameAlreadyExists() throws Exception {
    mockMvc
        .perform(
            post(registerUserUri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(takenUserCredentialsJson))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errorMessage").isNotEmpty());
  }

  @Test
  @Transactional
  void registerUser_ShouldReturn400WithErrorMessage_GivenInvalidData() throws Exception {
    mockMvc
        .perform(
            post(registerUserUri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(badUserCredentialsJson))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errorMessage").isNotEmpty());
  }

  @Test
  @Transactional
  void registerUser_ShouldReturnEachValidationError_GivenInvalidData() throws Exception {
    mockMvc
        .perform(
            post(registerUserUri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(badUserCredentialsJson))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errorDetails", hasSize(3)));
  }

  @Test
  @Transactional
  void passwordLogin_ShouldReturnSuccessfulResponse_GivenValidCredentials() throws Exception {
    mockMvc
        .perform(post(passwordLoginUri)
          .contentType(MediaType.APPLICATION_JSON)
          .content(user1CredentialsJson))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.authToken").isNotEmpty());
  }

  @Test
  @Transactional
  void passwordLogin_ShouldReturn400WithErrorResponse_GivenWrongCredentials() throws Exception {
    mockMvc
        .perform(post(passwordLoginUri)
            .contentType(MediaType.APPLICATION_JSON)
            .content(wrongUserCredentialsJson))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errorMessage").isNotEmpty());
  }

  @Test
  @Transactional
  void passwordLogin_ShouldReturn400WithErrorResponse_GivenInvalidData() throws Exception {
    mockMvc
        .perform(post(passwordLoginUri)
            .contentType(MediaType.APPLICATION_JSON)
            .content(badUserCredentialsJson))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errorMessage").isNotEmpty());
  }

  @Test
  @Transactional
  void passwordLogin_ShouldReturnEachValidationError_GivenInvalidData() throws Exception {
    mockMvc
        .perform(post(passwordLoginUri)
            .contentType(MediaType.APPLICATION_JSON)
            .content(badUserCredentialsJson))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errorDetails", hasSize(3)));
  }
}
