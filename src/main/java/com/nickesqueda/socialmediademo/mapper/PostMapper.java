package com.nickesqueda.socialmediademo.mapper;

import com.nickesqueda.socialmediademo.dto.PostDto;
import com.nickesqueda.socialmediademo.entity.Post;

public class PostMapper {
  public static Post toEntity(PostDto postDto) {
    Post postEntity = new Post();
    postEntity.setId(postDto.getId());
    postEntity.setContent(postDto.getContent());
    return postEntity;
  }

  public static PostDto toDto(Post post) {
    PostDto postDto = new PostDto();
    postDto.setId(post.getId());
    postDto.setContent(post.getContent());
    return postDto;
  }
}
