package com.example.blog.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class Post {

    public Post(Long id, String title, String content, byte[] images, LocalDateTime created, LocalDateTime updated) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.image = images;
        this.created = created;
        this.updated = updated;
    }

    private Long id;
    private String title;
    private String content;
    private byte[] image;
    private LocalDateTime created;
    private LocalDateTime updated;
    private Set<Comment> comments = new LinkedHashSet<>();
    private Set<Like> likes = new LinkedHashSet<>();
    private Set<Tag> tags = new LinkedHashSet<>();
}
