package com.nickesqueda.socialmediademo.security;

import com.nickesqueda.socialmediademo.entity.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.stream.Collectors;

import static com.nickesqueda.socialmediademo.security.SecurityConstants.ROLES;
import static com.nickesqueda.socialmediademo.security.SecurityConstants.SUBJECT;

public class JwtUtils {
  // TODO: extract into config and secret management.
  private static final String SECRET =
      "507c4db58311630bdfa4ed5d4b8a562ca2f43370e03a3df411b3784a805681f7";

  public static String createJwt(Authentication authentication) {
    String username = authentication.getName();
    Map<String, Object> claims = Map.of(SUBJECT, username, ROLES, authentication.getAuthorities());
    return generateJwt(username, claims);
  }

  private static String generateJwt(String subject, Map<String, Object> claims) {
    return Jwts.builder()
        .setSubject(subject)
        .setClaims(claims)
        .setIssuedAt(
            new Date(
                System.currentTimeMillis())) // TODO: does we need to pass in Sys.currTimeMillis()?
        .setExpiration(new Date(System.currentTimeMillis() + (1000 * 60 * 30)))
        .signWith(getSignKey())
        .compact();
  }

  private static SecretKey getSignKey() {
    byte[] keyInBytes = Decoders.BASE64.decode(SECRET);
    return Keys.hmacShaKeyFor(keyInBytes);
  }

  public static String extractUsername(String token) {
    Claims claims = extractAllClaimsAndValidateJwt(token);
    return claims.getSubject();
  }

  public static Collection<GrantedAuthority> extractGrantedAuthorities(String token) {
    Claims claims = extractAllClaimsAndValidateJwt(token);
    // TODO: handle unchecked cast warning.
    List<Map<String, String>> roles = (List<Map<String, String>>) claims.get(ROLES);
    return roles.stream().map(Role::new).collect(Collectors.toList());
  }

  private static Claims extractAllClaimsAndValidateJwt(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(getSignKey())
        .build()
        .parseClaimsJws(token) // validates the JWT signature and expiration.
        .getBody();
  }
}
