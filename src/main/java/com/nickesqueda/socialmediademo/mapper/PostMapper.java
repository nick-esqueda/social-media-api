package com.nickesqueda.socialmediademo.mapper;

import com.nickesqueda.socialmediademo.dto.PostDto;
import com.nickesqueda.socialmediademo.entity.Post;

public class PostMapper {
  public static Post toEntity(PostDto postDto) {
    Post postEntity = new Post();
    postEntity.setId(postDto.getId());
    postEntity.setContent(postDto.getContent());
    // don't set createdAt/updatedAt. allow Spring Data JPA to set these automatically.
    return postEntity;
  }

  public static PostDto toDto(Post post) {
    PostDto postDto = new PostDto();
    postDto.setId(post.getId());
    postDto.setContent(post.getContent());
    postDto.setCreatedAt(post.getCreatedAt());
    postDto.setUpdatedAt(post.getUpdatedAt());
    return postDto;
  }
}
