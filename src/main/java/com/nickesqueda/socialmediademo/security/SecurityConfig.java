package com.nickesqueda.socialmediademo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
  private final JwtAuthFilter jwtAuthFilter;

  public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
    this.jwtAuthFilter = jwtAuthFilter;
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
    // TODO: why returning 401 instead of 404 for unhandled routes?
    return httpSecurity
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
        .sessionManagement( // stateless session management for JWT auth.
            (httpSecuritySessionManagementConfigurer ->
                httpSecuritySessionManagementConfigurer.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS)))
        .authorizeHttpRequests(
            authorizationManagerRequestMatcherRegistry -> {
              authorizationManagerRequestMatcherRegistry
                  .requestMatchers("/api/v1/auth/**")
                  .permitAll()
                  .requestMatchers(HttpMethod.GET)
                  .permitAll()
                  .anyRequest()
                  .authenticated();
            })
        .csrf(AbstractHttpConfigurer::disable) // TODO: enable CSRF protection.
        .httpBasic(Customizer.withDefaults())
        .build();
  }

  @Bean
  public AuthenticationManager authenticationManager(
      UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
    DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
    authenticationProvider.setUserDetailsService(userDetailsService);
    authenticationProvider.setPasswordEncoder(passwordEncoder);
    return new ProviderManager(authenticationProvider);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
