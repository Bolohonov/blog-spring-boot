package com.example.blog.mapper;

import com.example.blog.controller.request.CommentRequest;
import com.example.blog.model.Comment;
import com.example.blog.model.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(target = "postId", source = "post.id")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created", ignore = true)
    Comment toComment(CommentRequest request, Post post);
}
