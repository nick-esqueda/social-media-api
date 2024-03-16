package com.nickesqueda.socialmediademo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleGenericException(RuntimeException e) {
        System.out.println("inside global generic exception handler");
        return ResponseEntity.internalServerError().body("Unhandled exception: " + e.getMessage());
    }
}
