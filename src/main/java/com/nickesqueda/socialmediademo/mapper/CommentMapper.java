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
    return commentEntity;
  }

  public static CommentDto toDto(Comment commentEntity) {
    CommentDto commentDto = new CommentDto();
    commentDto.setId(commentEntity.getId());
    commentDto.setContent(commentEntity.getContent());
    return commentDto;
  }
}
