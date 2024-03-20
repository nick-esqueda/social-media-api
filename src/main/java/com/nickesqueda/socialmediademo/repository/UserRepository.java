package com.nickesqueda.socialmediademo.repository;

import com.nickesqueda.socialmediademo.entity.UserEntity;
import com.nickesqueda.socialmediademo.exception.ResourceNotFoundException;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
  Optional<UserEntity> findByUsername(String username);

  boolean existsByUsername(String username);

  default UserEntity retrieveOrElseThrow(Long userId) {
    return findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException(UserEntity.class, userId));
  }
}
