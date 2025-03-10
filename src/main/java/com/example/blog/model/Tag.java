package org.example.blog.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tag {

    public Tag(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    private Long id;
    private String name;
    private Long postId;
}
