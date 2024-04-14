package com.nickesqueda.socialmediademo.entity;

import static com.nickesqueda.socialmediademo.util.ValidationConstants.COMMENT_MAX_LENGTH;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "comments")
public class Comment extends BaseEntity {
  @Column(nullable = false, length = COMMENT_MAX_LENGTH)
  private String content;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "post_id", nullable = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Post post;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  private UserEntity user;
}
