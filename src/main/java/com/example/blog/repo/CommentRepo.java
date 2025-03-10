package org.example.blog.repo;

import org.example.blog.model.Comment;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepo {
    void save(Comment comment);
    void delete(Long id);
    List<Comment> findByPostIds(List<Long> postIds);
}
