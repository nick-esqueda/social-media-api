package com.nickesqueda.socialmediademo.security;

import static com.nickesqueda.socialmediademo.security.SecurityConstants.*;

import com.nickesqueda.socialmediademo.entity.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.*;
import java.util.stream.Collectors;
import javax.crypto.SecretKey;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

public class JwtUtils {
  // TODO: extract into config and secret management.
  private static final String SECRET =
      "507c4db58311630bdfa4ed5d4b8a562ca2f43370e03a3df411b3784a805681f7";

  public static String generateJwt(Authentication authentication) {
    UserPrincipal currentUser = (UserPrincipal) authentication.getPrincipal();
    String userId = currentUser.getId().toString();
    Map<String, Object> claims = createClaims(currentUser);
    return generateJwt(userId, claims);
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

  public static Long extractUserId(String token) {
    Claims claims = extractAllClaimsAndValidateJwt(token);
    return Long.valueOf(claims.getSubject());
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

  private static SecretKey getSignKey() {
    byte[] keyInBytes = Decoders.BASE64.decode(SECRET);
    return Keys.hmacShaKeyFor(keyInBytes);
  }

  public static Map<String, Object> createClaims(UserPrincipal userPrincipal) {
    Map<String, Object> claims = new HashMap<>();
    claims.put(SUBJECT, userPrincipal.getId());
    claims.put(USERNAME, userPrincipal.getUsername());
    claims.put(ROLES, userPrincipal.getAuthorities());
    return claims;
  }
}
