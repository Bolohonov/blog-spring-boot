package com.example.blog.service;

import com.example.blog.model.Like;
import com.example.blog.model.Post;
import com.example.blog.repo.LikeRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LikeServiceTest {

    @Mock
    private LikeRepo likeRepo;

    @InjectMocks
    private LikeService likeService;

    private Post post;

    @BeforeEach
    public void setUp() {
        post = new Post();
        post.setId(1L);
    }

    @Test
    public void testAddLike() {
        Like like = new Like();
        like.setPostId(post.getId());

        doNothing().when(likeRepo).save(any(Like.class));

        likeService.addLike(post);

        verify(likeRepo, times(1)).save(any(Like.class));
    }

    @Test
    public void testGetByPostIds() {
        List<Long> postIds = List.of(1L);
        Like like = new Like();
        like.setPostId(1L);
        List<Like> likeList = List.of(like);
        Map<Long, List<Like>> expectedMap = Map.of(1L, likeList);

        when(likeRepo.findByPostIds(postIds)).thenReturn(likeList);

        Map<Long, List<Like>> result = likeService.getByPostIds(postIds);

        assertEquals(expectedMap, result);
        verify(likeRepo, times(1)).findByPostIds(postIds);
    }
}
