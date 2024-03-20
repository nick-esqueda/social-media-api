package com.nickesqueda.socialmediademo.service;

import com.nickesqueda.socialmediademo.dto.UserCredentials;
import com.nickesqueda.socialmediademo.entity.Role;
import com.nickesqueda.socialmediademo.entity.UserEntity;
import com.nickesqueda.socialmediademo.repository.RoleRepository;
import com.nickesqueda.socialmediademo.repository.UserRepository;
import com.nickesqueda.socialmediademo.security.AuthUtils;
import com.nickesqueda.socialmediademo.security.JwtUtils;
import java.util.Collection;
import java.util.Collections;
import javax.management.relation.RoleNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
  private final AuthenticationManager authenticationManager;
  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;

  public AuthService(
      AuthenticationManager authenticationManager,
      UserRepository userRepository,
      RoleRepository roleRepository,
      PasswordEncoder passwordEncoder) {
    this.authenticationManager = authenticationManager;
    this.passwordEncoder = passwordEncoder;
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
  }

  public String registerNewUser(UserCredentials userCredentials) throws RoleNotFoundException {
    String username = userCredentials.getUsername();
    String password = userCredentials.getPassword();

    // construct a new user entity and save it in the DB.
    UserEntity userEntity = new UserEntity();
    userEntity.setUsername(username);
    userEntity.setPasswordHash(passwordEncoder.encode(password));
    // TODO: separate logic for different roles (admin/business user/etc.)
    Role role = roleRepository.findByRoleName("USER").orElseThrow(RoleNotFoundException::new);
    Collection<Role> roles = Collections.singletonList(role);
    userEntity.setRoles(roles);
    userRepository.save(userEntity);

    // populate SecurityContext with the newly authenticated user.
    Authentication authentication =
        new UsernamePasswordAuthenticationToken(username, password, roles);
    AuthUtils.populateSpringSecurityContext(authentication);

    // create JWT and send in response so user can be re-authenticated.
    return JwtUtils.createJwt(authentication);
  }

  public String authenticateUser(UserCredentials userCredentials) {
    String username = userCredentials.getUsername();
    String password = userCredentials.getPassword();

    // authenticate user with Spring Security.
    // throws exception if user can't be authenticated.
    UsernamePasswordAuthenticationToken token =
        new UsernamePasswordAuthenticationToken(username, password);
    Authentication authentication = authenticationManager.authenticate(token);
    // populate SecurityContext with the currently authenticated user.
    AuthUtils.populateSpringSecurityContext(authentication);

    // create JWT and send in response so user can be re-authenticated.
    return JwtUtils.createJwt(authentication);
  }
}
