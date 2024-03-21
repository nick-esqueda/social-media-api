package com.nickesqueda.socialmediademo.exception;

public class UsernameUnavailableException extends RuntimeException {
  public UsernameUnavailableException(String username) {
    super("Username '" + username + "' is already taken");
  }
}
