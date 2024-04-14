package com.nickesqueda.socialmediademo.entity;

import static com.nickesqueda.socialmediademo.util.ValidationConstants.*;

import com.nickesqueda.socialmediademo.model.Gender;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Collection;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "users")
public class UserEntity extends BaseEntity {
  @Column(nullable = false, unique = true, length = USERNAME_MAX_LENGTH)
  private String username;

  @Column(nullable = false)
  private String passwordHash;

  @Column(length = FIRST_NAME_MAX_LENGTH)
  private String firstName;

  @Column(length = LAST_NAME_MAX_LENGTH)
  private String lastName;

  @Column(unique = true, length = EMAIL_MAX_LENGTH)
  private String email;

  @Column(unique = true, length = PHONE_NUMBER_LENGTH)
  private String phoneNumber;

  @Column private LocalDate birthday;

  @Column
  @Enumerated(EnumType.STRING)
  private Gender gender;

  @Column(length = BIO_MAX_LENGTH)
  private String bio;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "users_roles",
      joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
  private Collection<Role> roles;
}
