package com.nickesqueda.socialmediademo.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<String> handleGenericException(RuntimeException e) {
    System.out.println("inside global generic exception handler");
    return ResponseEntity.internalServerError().body("Unhandled exception: " + e.toString());
  }

  @ExceptionHandler(UnauthorizedOperationException.class)
  public ResponseEntity<ErrorResponse> handleUnauthorizedOperationException(
      UnauthorizedOperationException e) {
    ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
    return ResponseEntity.status(UNAUTHORIZED).body(errorResponse);
  }
}
