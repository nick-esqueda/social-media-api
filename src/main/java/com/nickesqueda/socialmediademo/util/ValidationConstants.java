package com.nickesqueda.socialmediademo.util;

public class ValidationConstants {
  public static final int BIO_MIN_LENGTH = 1;
  public static final int BIO_MAX_LENGTH = 255;
  public static final int COMMENT_MIN_LENGTH = 1;
  public static final int COMMENT_MAX_LENGTH = 255;
  public static final int EMAIL_MAX_LENGTH = 320;
  public static final int FIRST_NAME_MIN_LENGTH = 2;
  public static final int FIRST_NAME_MAX_LENGTH = 50;
  public static final int LAST_NAME_MIN_LENGTH = 2;
  public static final int LAST_NAME_MAX_LENGTH = 50;
  public static final int PASSWORD_MIN_LENGTH = 8;
  public static final int PASSWORD_MAX_LENGTH = 64;
  public static final int PHONE_NUMBER_LENGTH = 10;
  public static final String PHONE_NUMBER_PATTERN = "^\\d{10}$";
  public static final String PHONE_NUMBER_PATTERN_MESSAGE = "must be 10 digits";
  public static final int POST_MIN_LENGTH = 1;
  public static final int POST_MAX_LENGTH = 255;
  public static final int USERNAME_MIN_LENGTH = 2;
  public static final int USERNAME_MAX_LENGTH = 32;
  public static final String USERNAME_PATTERN = "^[a-zA-Z0-9_-]+$";
  public static final String USERNAME_PATTERN_MESSAGE =
      "can only contain letters, numbers, underscores (_), and hyphens (-)";
}
