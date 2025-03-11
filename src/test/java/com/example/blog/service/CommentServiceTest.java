package com.example.blog.service;

import com.example.blog.controller.request.CommentRequest;
import com.example.blog.mapper.CommentMapper;
import com.example.blog.model.Comment;
import com.example.blog.model.Post;
import com.example.blog.repo.CommentRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private CommentRepo commentRepo;

    @InjectMocks
    private CommentService commentService;

    private Post post;
    private CommentRequest commentRequest;
    private Comment comment;

    @BeforeEach
    public void setUp() {
        post = new Post();
        post.setId(1L);

        commentRequest = new CommentRequest();
        commentRequest.setText("Sample Comment");

        comment = new Comment();
        comment.setId(1L);
        comment.setPostId(1L);
        comment.setText("Sample Comment");
    }

    @Test
    public void testSave() {
        when(commentMapper.toComment(commentRequest, post)).thenReturn(comment);
        doNothing().when(commentRepo).save(any(Comment.class));

        commentService.save(commentRequest, post);

        verify(commentMapper, times(1)).toComment(commentRequest, post);
        verify(commentRepo, times(1)).save(comment);
    }

    @Test
    public void testRemove() {
        doNothing().when(commentRepo).delete(anyLong());

        commentService.remove(comment);

        verify(commentRepo, times(1)).delete(comment.getId());
    }

    @Test
    public void testGetByPostIds() {
        List<Long> postIds = List.of(1L);
        List<Comment> commentList = List.of(comment);
        Map<Long, List<Comment>> expectedMap = commentList.stream().collect(Collectors.groupingBy(Comment::getPostId));

        when(commentRepo.findByPostIds(postIds)).thenReturn(commentList);

        Map<Long, List<Comment>> result = commentService.getByPostIds(postIds);

        assertEquals(expectedMap, result);
        verify(commentRepo, times(1)).findByPostIds(postIds);
    }
}
