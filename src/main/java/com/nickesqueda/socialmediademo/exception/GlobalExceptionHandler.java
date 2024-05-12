package com.nickesqueda.socialmediademo.exception;

import static org.springframework.http.HttpStatus.*;

import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
  @ResponseStatus(NOT_FOUND)
  @ExceptionHandler(ResourceNotFoundException.class)
  public ErrorResponse handleResourceNotFoundException(ResourceNotFoundException e) {
    return ErrorResponse.builder().errorMessage(e.getMessage()).build();
  }

  @ResponseStatus(FORBIDDEN)
  @ExceptionHandler(UnauthorizedOperationException.class)
  public ErrorResponse handleUnauthorizedOperationException(UnauthorizedOperationException e) {
    return ErrorResponse.builder().errorMessage(e.getMessage()).build();
  }

  @ResponseStatus(BAD_REQUEST)
  @ExceptionHandler(UsernameNotAvailableException.class)
  public ErrorResponse handleUsernameNotAvailableException(UsernameNotAvailableException e) {
    return ErrorResponse.builder().errorMessage(e.getMessage()).build();
  }

  @ResponseStatus(BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    List<String> errorMessages =
        e.getBindingResult().getFieldErrors().stream()
            .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
            .toList();
    return ErrorResponse.builder()
        .errorMessage("Input validation failed")
        .errorDetails(errorMessages)
        .build();
  }

  @ResponseStatus(BAD_REQUEST)
  @ExceptionHandler(DataIntegrityViolationException.class)
  public ErrorResponse handleDataIntegrityViolationException(DataIntegrityViolationException e) {
    return ErrorResponse.builder().errorMessage("Database constraint violated").build();
  }

  @ResponseStatus(BAD_REQUEST)
  @ExceptionHandler(BadCredentialsException.class)
  public ErrorResponse handleBadCredentialsException(BadCredentialsException e) {
    return ErrorResponse.builder().errorMessage(e.getMessage()).build();
  }

  @ResponseStatus(INTERNAL_SERVER_ERROR)
  @ExceptionHandler(RuntimeException.class)
  public ErrorResponse handleGenericException(RuntimeException e) {
    return ErrorResponse.builder().errorMessage("Unhandled exception: " + e.toString()).build();
  }
}
