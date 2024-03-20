package com.nickesqueda.socialmediademo.repository;

import com.nickesqueda.socialmediademo.entity.Role;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
  Optional<Role> findByRoleName(String roleName);
}
