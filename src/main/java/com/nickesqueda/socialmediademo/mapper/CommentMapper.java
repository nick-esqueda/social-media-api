package com.nickesqueda.socialmediademo.mapper;

import com.nickesqueda.socialmediademo.dto.CommentDto;
import com.nickesqueda.socialmediademo.entity.Comment;

public class CommentMapper {
  public static Comment toEntity(CommentDto commentDto) {
    Comment commentEntity = new Comment();
    commentEntity.setId(commentDto.getId());
    commentEntity.setContent(commentDto.getContent());
    return commentEntity;
  }

  public static CommentDto toDto(Comment commentEntity) {
    CommentDto commentDto = new CommentDto();
    commentDto.setId(commentEntity.getId());
    commentDto.setContent(commentEntity.getContent());
    return commentDto;
  }
}
