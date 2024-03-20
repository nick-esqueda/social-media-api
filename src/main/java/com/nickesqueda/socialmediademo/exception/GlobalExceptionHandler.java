package com.nickesqueda.socialmediademo.exception;

import static org.springframework.http.HttpStatus.*;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
  @ResponseStatus(NOT_FOUND)
  @ExceptionHandler(ResourceNotFoundException.class)
  public ErrorResponse handleResourceNotFoundException(ResourceNotFoundException e) {
    return new ErrorResponse(e.getMessage());
  }

  @ResponseStatus(UNAUTHORIZED)
  @ExceptionHandler(UnauthorizedOperationException.class)
  public ErrorResponse handleUnauthorizedOperationException(UnauthorizedOperationException e) {
    return new ErrorResponse(e.getMessage());
  }

  @ResponseStatus(INTERNAL_SERVER_ERROR)
  @ExceptionHandler(RuntimeException.class)
  public ErrorResponse handleGenericException(RuntimeException e) {
    return new ErrorResponse("Unhandled exception: " + e.toString());
  }
}
