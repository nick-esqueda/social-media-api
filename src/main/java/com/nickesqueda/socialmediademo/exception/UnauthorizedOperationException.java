package com.nickesqueda.socialmediademo.exception;

public class UnauthorizedOperationException extends RuntimeException {
  public UnauthorizedOperationException(String message) {
    super(message);
  }
}
