package com.example.blog.repo;

import com.example.blog.model.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface TagRepo {
    List<Tag> save(List<Tag> tags);
    void batchUpdateByPostId(Long postId, Set<Tag> tags);
    List<Tag> getOrCreate(Set<String> tagNames);
    List<Tag> findAll();
    List<Tag> findByPostIds(List<Long> postIds);
}
