package com.nickesqueda.socialmediademo.service;

import com.nickesqueda.socialmediademo.entity.UserEntity;
import com.nickesqueda.socialmediademo.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public void createUser(UserEntity user) {
    userRepository.save(user);
  }
}
