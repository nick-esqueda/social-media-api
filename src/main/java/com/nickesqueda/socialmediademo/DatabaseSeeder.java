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
import java.util.Collections;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DatabaseSeeder implements CommandLineRunner {
  private final RoleRepository roleRepository;
  private final UserRepository userRepository;
  private final PostRepository postRepository;
  private final CommentRepository commentRepository;
  private final PasswordEncoder passwordEncoder;

  public DatabaseSeeder(
      RoleRepository roleRepository,
      UserRepository userRepository,
      PostRepository postRepository,
      CommentRepository commentRepository,
      PasswordEncoder passwordEncoder) {
    this.roleRepository = roleRepository;
    this.userRepository = userRepository;
    this.postRepository = postRepository;
    this.commentRepository = commentRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public void run(String... args) throws Exception {
    if (roleRepository.count() == 0) {
      Role adminRole = new Role(ADMIN);
      Role userRole = new Role(USER);
      roleRepository.save(adminRole);
      roleRepository.save(userRole);

      String password = passwordEncoder.encode("password");
      UserEntity user1 = new UserEntity("user1", password, Collections.singletonList(userRole));
      UserEntity user2 = new UserEntity("user2", password, Collections.singletonList(userRole));
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
