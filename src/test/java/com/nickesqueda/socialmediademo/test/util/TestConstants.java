package com.nickesqueda.socialmediademo.test.util;

public class TestConstants {
  public static final String TEST_STRING = "TEST";
  public static final String TEST_STRING2 = "TEST2";
  public static final String TEST_USERNAME = "user1";

  public static final String updateUserRequestJson =
      """
      {
        "username": "%s",
        "firstName": "%s",
        "lastName": "%s"
      }"""
          .formatted(TEST_USERNAME, TEST_STRING, TEST_STRING);

  public static final String updateUserBadRequestJson =
      """
      {
        "username": "!",
        "firstName": "",
        "lastName": "",
        "email": "not an email",
        "phoneNumber": "not a phone number",
        "birthday": "9999-12-31",
        "gender": null,
        "bio": ""
      }""";

  public static final String createPostRequestJson =
      """
      {
        "content": "%s"
      }"""
          .formatted(TEST_STRING);

  public static final String createPostBadRequestJson =
      """
      {
        "content": ""
      }""";

  public static final String updatePostRequestJson =
      """
      {
          "content": "%s"
      }"""
          .formatted(TEST_STRING2);

  public static final String updatePostBadRequestJson =
      """
      {
          "content": ""
      }""";

  public static final String createCommentRequestJson =
      """
      {
          "content": "%s"
      }"""
          .formatted(TEST_STRING);

  public static final String createCommentBadRequestJson =
      """
      {
          "content": ""
      }""";

  public static final String updateCommentRequestJson =
      """
      {
          "content": "%s"
      }"""
          .formatted(TEST_STRING2);

  public static final String updateCommentBadRequestJson =
      """
      {
          "content": ""
      }""";
}
