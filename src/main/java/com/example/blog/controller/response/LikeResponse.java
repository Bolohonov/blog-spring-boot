package com.example.blog.controller.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class LikeResponse {
    private Long userId;
    private LocalDateTime created;
}
