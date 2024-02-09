package com.nickesqueda.socialmediademo.repository;

import com.nickesqueda.socialmediademo.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
  Optional<UserEntity> findByUsername(String username);
  boolean existsByUsername(String username);
}
