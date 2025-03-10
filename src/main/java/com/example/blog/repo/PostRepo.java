package com.example.blog.repo;

import com.example.blog.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepo {
    Long saveWithoutTags(Post post);
    Post getById(Long id);
    void deleteById(Long id);
    void updateWithoutTags(Post post);
    Page<Post> findAll(Pageable pageable);
}
