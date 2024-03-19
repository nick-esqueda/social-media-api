package com.nickesqueda.socialmediademo.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<String> handleGenericException(RuntimeException e) {
    System.out.println("inside global generic exception handler");
    return ResponseEntity.internalServerError().body("Unhandled exception: " + e.toString());
  }

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
}
