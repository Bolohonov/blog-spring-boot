package com.example.blog.mapper;

import com.example.blog.controller.response.TagResponse;
import com.example.blog.model.Tag;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TagMapper {
    TagResponse toResponse(Tag tag);
    List<TagResponse> toResponses(List<Tag> tags);
}
