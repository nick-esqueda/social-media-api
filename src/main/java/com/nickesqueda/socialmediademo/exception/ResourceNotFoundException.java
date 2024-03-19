package com.nickesqueda.socialmediademo.exception;

import lombok.Getter;

@Getter
public class ResourceNotFoundException extends RuntimeException {
  private final Class<?> entityClass;
  private final int entityId;

  public ResourceNotFoundException(Class<?> entityClass, int entityId) {
    super("Resource not found: " + entityClass.getSimpleName() + " #" + entityId);
    this.entityClass = entityClass;
    this.entityId = entityId;
  }
}
