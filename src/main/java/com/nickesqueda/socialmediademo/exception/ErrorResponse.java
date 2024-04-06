package com.nickesqueda.socialmediademo.exception;

import java.util.Collection;
import lombok.Builder;

@Builder
public record ErrorResponse(String errorMessage, Collection<?> errorDetails) {}
