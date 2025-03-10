package com.example.blog.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Like {
    private Long id;
    private Long postId;
    private Long userId;
    private LocalDateTime created;
}
