package com.example.blog.service;

import com.example.blog.controller.request.CommentRequest;
import com.example.blog.controller.request.PostRequest;
import com.example.blog.controller.response.PostResponse;
import com.example.blog.mapper.PostMapper;
import com.example.blog.model.Comment;
import com.example.blog.model.Post;
import com.example.blog.repo.PostRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepo postRepo;

    @Mock
    private PostMapper postMapper;

    @Mock
    private CommentService commentService;

    @Mock
    private LikeService likeService;

    @Mock
    private TagService tagService;

    @InjectMocks
    private PostService postService;

    @Test
    void save() {
        PostRequest request = new PostRequest();
        Post post = new Post();
        when(postMapper.toPost(request)).thenReturn(post);
        when(postRepo.saveWithoutTags(post)).thenReturn(1L);

        postService.save(request);

        verify(postMapper).toPost(request);
        verify(postRepo).saveWithoutTags(post);
        verify(tagService).batchUpdateByPostId(1L, post.getTags());
    }

    @Test
    void getById() {
        Post post = new Post();
        PostResponse postResponse = PostResponse.builder()
                .id(2L)
                .build();
        when(postRepo.getById(1L)).thenReturn(post);
        when(postMapper.toResponse(post)).thenReturn(postResponse);

        PostResponse result = postService.getById(1L);

        assertEquals(postResponse, result);
    }

    @Test
    void removeById() {
        postService.removeById(1L);

        verify(postRepo).deleteById(1L);
    }

    @Test
    void update() {
        Post post = new Post();
        PostRequest request = new PostRequest();
        when(postRepo.getById(1L)).thenReturn(post);
        when(tagService.save(request.getTags())).thenReturn(post.getTags());

        postService.update(1L, request);

        verify(postRepo).getById(1L);
        verify(postRepo).updateWithoutTags(post);
        verify(tagService).batchUpdateByPostId(post.getId(), post.getTags());
    }

    @Test
    void addComment() {
        Post post = new Post();
        CommentRequest commentRequest = new CommentRequest();
        when(postRepo.getById(1L)).thenReturn(post);

        postService.addComment(1L, commentRequest);

        verify(commentService).save(commentRequest, post);
    }

    @Test
    void deleteComment() {
        Post post = new Post();
        post.setComments(Set.of(Comment.builder().id(1L).build()));
        when(postRepo.getById(1L)).thenReturn(post);

        postService.deleteComment(1L, 1L);
    }

    @Test
    void like() {
        Post post = new Post();
        when(postRepo.getById(1L)).thenReturn(post);

        postService.like(1L);

        verify(likeService).addLike(post);
    }

    @Test
    void getPosts() {
        Pageable pageable = PageRequest.of(0, 10);
        Post post = new Post();
        post.setId(1L);
        post.setTitle("Sample Post");
        PostResponse postResponse = PostResponse.builder().build();
        List<Post> postList = List.of(post);
        Page<Post> postPage = new PageImpl<>(postList);
        when(postRepo.findAll(pageable)).thenReturn(postPage);
        when(tagService.getByPostIds(anyList())).thenReturn(Collections.emptyMap());
        when(commentService.getByPostIds(anyList())).thenReturn(Collections.emptyMap());
        when(likeService.getByPostIds(anyList())).thenReturn(Collections.emptyMap());
        when(postMapper.toResponse(any(Post.class))).thenReturn(postResponse);

        Page<PostResponse> result = postService.getPosts(pageable);

        assertEquals(1, result.getTotalElements());
    }
}