package com.example.blog.controller.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class CommentResponse {
    private Long id;
    private String text;
    private LocalDateTime created;
    private LocalDateTime updated;
}
