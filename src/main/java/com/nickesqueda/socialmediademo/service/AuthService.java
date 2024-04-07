package com.nickesqueda.socialmediademo.service;

import static com.nickesqueda.socialmediademo.security.SecurityConstants.USER;

import com.nickesqueda.socialmediademo.dto.LoginResponseDto;
import com.nickesqueda.socialmediademo.dto.RegistrationResponseDto;
import com.nickesqueda.socialmediademo.dto.UserCredentialsDto;
import com.nickesqueda.socialmediademo.entity.Role;
import com.nickesqueda.socialmediademo.entity.UserEntity;
import com.nickesqueda.socialmediademo.exception.UsernameNotAvailableException;
import com.nickesqueda.socialmediademo.mapper.AuthMapper;
import com.nickesqueda.socialmediademo.repository.RoleRepository;
import com.nickesqueda.socialmediademo.repository.UserRepository;
import com.nickesqueda.socialmediademo.security.JwtUtils;
import com.nickesqueda.socialmediademo.security.UserPrincipal;
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

  public RegistrationResponseDto registerUser(UserCredentialsDto userCredentialsDto) {
    String username = userCredentialsDto.getUsername();
    if (userRepository.existsByUsername(username)) {
      throw new UsernameNotAvailableException(username);
    }

    Role role = roleRepository.retrieveByRoleNameOrElseThrow(USER);
    String passwordHash = passwordEncoder.encode(userCredentialsDto.getPassword());
    UserEntity userEntity = AuthMapper.toEntity(userCredentialsDto, passwordHash, role);
    userRepository.save(userEntity);

    LoginResponseDto loginResponseDto = passwordLogin(userCredentialsDto);
    return AuthMapper.toDto(loginResponseDto, userEntity.getRoles());
  }

  public LoginResponseDto passwordLogin(UserCredentialsDto userCredentialsDto) {
    Authentication authentication = authenticateUser(userCredentialsDto);
    String authToken = JwtUtils.generateJwt(authentication);
    UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
    return AuthMapper.toDto(principal.getUserEntity(), authToken);
  }

  public Authentication authenticateUser(UserCredentialsDto userCredentialsDto) {
    String username = userCredentialsDto.getUsername();
    String password = userCredentialsDto.getPassword();
    Authentication authentication =
        UsernamePasswordAuthenticationToken.unauthenticated(username, password);
    return authenticationManager.authenticate(authentication);
  }
}
