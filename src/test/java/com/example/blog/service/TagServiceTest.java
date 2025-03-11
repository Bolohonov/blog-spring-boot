package com.example.blog.service;

import com.example.blog.controller.response.TagResponse;
import com.example.blog.mapper.TagMapper;
import com.example.blog.model.Tag;
import com.example.blog.repo.TagRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TagServiceTest {

    @Mock
    private TagRepo tagRepo;

    @Mock
    private TagMapper tagMapper;

    @InjectMocks
    private TagService tagService;

    private Tag tag;
    private Tag tag2;
    private TagResponse tagResponse;

    @BeforeEach
    public void setUp() {
        tagService = new TagService(tagRepo, tagMapper);
        tag = new Tag(1L, "test");
        tag.setPostId(1L);
        tag2 = new Tag(2L, "test2");
        tag2.setPostId(2L);
        tagResponse = TagResponse.builder().name("test").build();
    }

    @Test
    public void testSave() {
        String tags = "test, example";
        Set<String> tagsFrom = Set.of("test", "example");
        Set<Tag> tagSet = Set.of(tag);

        when(tagRepo.getOrCreate(tagsFrom)).thenReturn(new ArrayList<>(tagSet));

        Set<Tag> savedTags = tagService.save(tags);

        assertEquals(tagSet, savedTags);
        verify(tagRepo, times(1)).getOrCreate(tagsFrom);
    }

    @Test
    public void testGetAllTags() {
        List<Tag> tagList = List.of(tag, tag2);
        List<TagResponse> tagResponseList = List.of(tagResponse);

        when(tagRepo.findAll()).thenReturn(tagList);
        when(tagMapper.toResponses(tagList)).thenReturn(tagResponseList);

        List<String> allTags = tagService.getAllTags();

        assertEquals(List.of("test"), allTags);
        verify(tagRepo, times(1)).findAll();
        verify(tagMapper, times(1)).toResponses(tagList);
    }

    @Test
    public void testGetByPostIds() {
        List<Long> postIds = List.of(1L, 2L);
        List<Tag> tagList = List.of(tag, tag2);
        List<Tag> tagList1 = List.of(tag);
        List<Tag> tagList2 = List.of(tag2);
        Map<Long, List<Tag>> expectedMap = Map.of(1L, tagList1, 2L, tagList2);

        when(tagRepo.findByPostIds(postIds)).thenReturn(tagList);

        Map<Long, List<Tag>> tagsByPostIds = tagService.getByPostIds(postIds);

        assertEquals(expectedMap, tagsByPostIds);
        verify(tagRepo, times(1)).findByPostIds(postIds);
    }

    @Test
    public void testBatchUpdateByPostId() {
        Long postId = 1L;
        Set<Tag> tags = Set.of(tag);

        doNothing().when(tagRepo).batchUpdateByPostId(postId, tags);

        tagService.batchUpdateByPostId(postId, tags);

        verify(tagRepo, times(1)).batchUpdateByPostId(postId, tags);
    }
}
