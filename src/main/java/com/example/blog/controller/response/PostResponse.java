package com.example.blog.controller.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Builder
public class PostResponse {
    private Long id;
    private String title;
    private String content;
    private String image;
    private LocalDateTime created;
    private LocalDateTime updated;
    private Set<CommentResponse> comments;
    private Set<LikeResponse> likes;
    private Set<TagResponse> tags;
}
