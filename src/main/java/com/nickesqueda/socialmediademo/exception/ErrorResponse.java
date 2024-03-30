package com.nickesqueda.socialmediademo.exception;

import lombok.Builder;

import java.util.Collection;

@Builder
public record ErrorResponse(String errorMessage, Collection<?> errorDetails) {}
