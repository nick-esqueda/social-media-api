package com.nickesqueda.socialmediademo.mapper;

import com.nickesqueda.socialmediademo.dto.CommentDto;
import com.nickesqueda.socialmediademo.entity.Comment;
import com.nickesqueda.socialmediademo.entity.Post;
import com.nickesqueda.socialmediademo.entity.UserEntity;

public class CommentMapper {
  public static Comment toEntity(CommentDto commentDto, Post postEntity, UserEntity userEntity) {
    Comment commentEntity = new Comment();
    commentEntity.setId(commentDto.getId());
    commentEntity.setContent(commentDto.getContent());
    commentEntity.setPost(postEntity);
    commentEntity.setUser(userEntity);
    // don't set createdAt/updatedAt. allow Spring Data JPA to set these automatically.
    return commentEntity;
  }

  public static CommentDto toDto(Comment commentEntity) {
    CommentDto commentDto = new CommentDto();
    commentDto.setId(commentEntity.getId());
    commentDto.setContent(commentEntity.getContent());
    commentDto.setCreatedAt(commentEntity.getCreatedAt());
    commentDto.setUpdatedAt(commentEntity.getUpdatedAt());
    return commentDto;
  }
}
