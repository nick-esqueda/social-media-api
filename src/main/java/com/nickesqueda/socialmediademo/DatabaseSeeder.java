package com.nickesqueda.socialmediademo;

import static com.nickesqueda.socialmediademo.security.SecurityConstants.*;

import com.nickesqueda.socialmediademo.entity.Comment;
import com.nickesqueda.socialmediademo.entity.Post;
import com.nickesqueda.socialmediademo.entity.Role;
import com.nickesqueda.socialmediademo.entity.UserEntity;
import com.nickesqueda.socialmediademo.repository.CommentRepository;
import com.nickesqueda.socialmediademo.repository.PostRepository;
import com.nickesqueda.socialmediademo.repository.RoleRepository;
import com.nickesqueda.socialmediademo.repository.UserRepository;
import java.util.Collection;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DatabaseSeeder implements CommandLineRunner {

  private final RoleRepository roleRepository;
  private final UserRepository userRepository;
  private final PostRepository postRepository;
  private final CommentRepository commentRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public void run(String... args) throws Exception {
    if (roleRepository.count() == 0) {
      Role adminRole = new Role(ADMIN);
      Role userRole = new Role(USER);
      roleRepository.save(adminRole);
      roleRepository.save(userRole);

      String password = passwordEncoder.encode("password");
      Collection<Role> roles = Collections.singletonList(userRole);
      UserEntity user1 =
          UserEntity.builder().username("user1").passwordHash(password).roles(roles).build();
      UserEntity user2 =
          UserEntity.builder().username("user2").passwordHash(password).roles(roles).build();
      userRepository.save(user1);
      userRepository.save(user2);

      Post post1 = new Post("This is user1's first post.", user1);
      Post post2 = new Post("This is user2's first post.", user2);
      postRepository.save(post1);
      postRepository.save(post2);

      Comment comment1 = new Comment("Hi user2, I'm user1.", post2, user1);
      Comment comment2 = new Comment("Hi user1, I'm user2.", post1, user2);
      commentRepository.save(comment1);
      commentRepository.save(comment2);
    }
  }
}
