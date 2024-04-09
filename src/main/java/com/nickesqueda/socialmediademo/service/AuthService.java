package com.nickesqueda.socialmediademo.service;

import static com.nickesqueda.socialmediademo.security.SecurityConstants.USER;

import com.nickesqueda.socialmediademo.dto.AuthCredentialsDto;
import com.nickesqueda.socialmediademo.dto.LoginResponseDto;
import com.nickesqueda.socialmediademo.dto.RegistrationResponseDto;
import com.nickesqueda.socialmediademo.entity.Role;
import com.nickesqueda.socialmediademo.entity.UserEntity;
import com.nickesqueda.socialmediademo.exception.UsernameNotAvailableException;
import com.nickesqueda.socialmediademo.repository.RoleRepository;
import com.nickesqueda.socialmediademo.repository.UserRepository;
import com.nickesqueda.socialmediademo.security.JwtUtils;
import com.nickesqueda.socialmediademo.security.UserPrincipal;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {

  private final AuthenticationManager authenticationManager;
  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final ModelMapper modelMapper;

  public RegistrationResponseDto registerUser(AuthCredentialsDto authCredentialsDto) {
    String username = authCredentialsDto.getUsername();
    if (userRepository.existsByUsername(username)) {
      throw new UsernameNotAvailableException(username);
    }

    UserEntity userEntity = createUserEntity(authCredentialsDto);
    userRepository.save(userEntity);

    LoginResponseDto loginResponseDto = passwordLogin(authCredentialsDto);

    RegistrationResponseDto registrationResponseDto =
        modelMapper.map(loginResponseDto, RegistrationResponseDto.class);
    registrationResponseDto.setRoles(userEntity.getRoles());
    return registrationResponseDto;
  }

  public LoginResponseDto passwordLogin(AuthCredentialsDto authCredentialsDto) {
    Authentication authentication = authenticateUser(authCredentialsDto);
    String authToken = JwtUtils.generateJwt(authentication);
    UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

    LoginResponseDto loginResponseDto =
        modelMapper.map(principal.getUserEntity(), LoginResponseDto.class);
    loginResponseDto.setAuthToken(authToken);
    return loginResponseDto;
  }

  public Authentication authenticateUser(AuthCredentialsDto authCredentialsDto) {
    String username = authCredentialsDto.getUsername();
    String password = authCredentialsDto.getPassword();
    Authentication authentication =
        UsernamePasswordAuthenticationToken.unauthenticated(username, password);
    return authenticationManager.authenticate(authentication);
  }

  public UserEntity createUserEntity(AuthCredentialsDto authCredentialsDto) {
    String passwordHash = passwordEncoder.encode(authCredentialsDto.getPassword());
    Role role = roleRepository.retrieveByRoleNameOrElseThrow(USER);
    return UserEntity.builder()
        .username(authCredentialsDto.getUsername())
        .passwordHash(passwordHash)
        .roles(Collections.singletonList(role))
        .build();
  }
}
