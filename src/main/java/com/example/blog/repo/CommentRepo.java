package com.example.blog.repo;

import com.example.blog.model.Comment;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepo {
    void save(Comment comment);
    void delete(Long id);
    List<Comment> findByPostIds(List<Long> postIds);
}
