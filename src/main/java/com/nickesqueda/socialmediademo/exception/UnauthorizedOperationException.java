package com.nickesqueda.socialmediademo.exception;

public class UnauthorizedOperationException extends RuntimeException {
  public UnauthorizedOperationException() {
    super("User is not authorized to perform this operation");
  }

  public UnauthorizedOperationException(String message) {
    super(message);
  }
}
