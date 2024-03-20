package com.nickesqueda.socialmediademo.repository;

import com.nickesqueda.socialmediademo.entity.UserEntity;
import com.nickesqueda.socialmediademo.exception.ResourceNotFoundException;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
  Optional<UserEntity> findByUsername(String username);

  boolean existsByUsername(String username);

  default UserEntity retrieveOrElseThrow(int userId) {
    return findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException(UserEntity.class, userId));
  }
}
