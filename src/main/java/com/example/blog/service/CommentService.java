package com.example.blog.service;

import lombok.RequiredArgsConstructor;
import com.example.blog.mapper.CommentMapper;
import com.example.blog.controller.request.CommentRequest;
import com.example.blog.model.Comment;
import com.example.blog.model.Post;
import com.example.blog.repo.CommentRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentMapper commentMapper;
    private final CommentRepo commentRepo;

    public void save(CommentRequest comment, Post post) {
        commentRepo.save(commentMapper.toComment(comment, post));
    }

    public void remove(Comment comment) {
        commentRepo.delete(comment.getId());
    }

    public Map<Long, List<Comment>> getByPostIds(List<Long> postIds) {
        List<Comment> comments = commentRepo.findByPostIds(postIds);
        return comments.stream().collect(Collectors.groupingBy(Comment::getPostId));
    }
}
