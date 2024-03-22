package com.nickesqueda.socialmediademo.exception;

public class UsernameNotAvailableException extends RuntimeException {
  public UsernameNotAvailableException(String username) {
    super("Username '" + username + "' is already taken");
  }
}
