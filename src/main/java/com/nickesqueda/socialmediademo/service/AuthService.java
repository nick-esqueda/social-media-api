package com.nickesqueda.socialmediademo.service;

import static com.nickesqueda.socialmediademo.security.SecurityConstants.USER;

import com.nickesqueda.socialmediademo.dto.LoginResponseDto;
import com.nickesqueda.socialmediademo.dto.RegistrationResponseDto;
import com.nickesqueda.socialmediademo.dto.UserCredentialsDto;
import com.nickesqueda.socialmediademo.entity.Role;
import com.nickesqueda.socialmediademo.entity.UserEntity;
import com.nickesqueda.socialmediademo.exception.UsernameNotAvailableException;
import com.nickesqueda.socialmediademo.repository.RoleRepository;
import com.nickesqueda.socialmediademo.repository.UserRepository;
import com.nickesqueda.socialmediademo.security.JwtUtils;
import com.nickesqueda.socialmediademo.security.UserPrincipal;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AuthService {
  private final AuthenticationManager authenticationManager;
  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final ModelMapper modelMapper;

  public AuthService(
      AuthenticationManager authenticationManager,
      UserRepository userRepository,
      RoleRepository roleRepository,
      PasswordEncoder passwordEncoder,
      ModelMapper modelMapper) {
    this.authenticationManager = authenticationManager;
    this.passwordEncoder = passwordEncoder;
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.modelMapper = modelMapper;
  }

  public RegistrationResponseDto registerUser(UserCredentialsDto userCredentialsDto) {
    String username = userCredentialsDto.getUsername();
    if (userRepository.existsByUsername(username)) {
      throw new UsernameNotAvailableException(username);
    }

    UserEntity userEntity = createUserEntity(userCredentialsDto);
    userRepository.save(userEntity);

    LoginResponseDto loginResponseDto = passwordLogin(userCredentialsDto);

    RegistrationResponseDto registrationResponseDto =
        modelMapper.map(loginResponseDto, RegistrationResponseDto.class);
    registrationResponseDto.setRoles(userEntity.getRoles());
    return registrationResponseDto;
  }

  public LoginResponseDto passwordLogin(UserCredentialsDto userCredentialsDto) {
    Authentication authentication = authenticateUser(userCredentialsDto);
    String authToken = JwtUtils.generateJwt(authentication);
    UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

    LoginResponseDto loginResponseDto =
        modelMapper.map(principal.getUserEntity(), LoginResponseDto.class);
    loginResponseDto.setAuthToken(authToken);
    return loginResponseDto;
  }

  public Authentication authenticateUser(UserCredentialsDto userCredentialsDto) {
    String username = userCredentialsDto.getUsername();
    String password = userCredentialsDto.getPassword();
    Authentication authentication =
        UsernamePasswordAuthenticationToken.unauthenticated(username, password);
    return authenticationManager.authenticate(authentication);
  }

  public UserEntity createUserEntity(UserCredentialsDto userCredentialsDto) {
    String passwordHash = passwordEncoder.encode(userCredentialsDto.getPassword());
    Role role = roleRepository.retrieveByRoleNameOrElseThrow(USER);
    return UserEntity.builder()
        .username(userCredentialsDto.getUsername())
        .passwordHash(passwordHash)
        .roles(Collections.singletonList(role))
        .build();
  }
}
