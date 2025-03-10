package com.example.blog.service;

import lombok.RequiredArgsConstructor;
import com.example.blog.mapper.TagMapper;
import com.example.blog.controller.response.TagResponse;
import com.example.blog.model.Tag;
import com.example.blog.repo.TagRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepo tagRepo;
    private final TagMapper tagMapper;

    @Transactional
    public Set<Tag> save(String tags) {
        Set<String> tagsFrom = Arrays.stream(tags.split(","))
                .map(String::trim)
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
        return new HashSet<>(tagRepo.getOrCreate(tagsFrom));
    }

    public List<String> getAllTags() {
        return  tagMapper.toResponses(tagRepo.findAll()).stream()
                .map(TagResponse::getName)
                .collect(Collectors.toList());
    }

    public Map<Long, List<Tag>> getByPostIds(List<Long> postIds) {
        List<Tag> tags = tagRepo.findByPostIds(postIds);
        return tags.stream().collect(Collectors.groupingBy(Tag::getPostId));
    }

    @Transactional
    public void batchUpdateByPostId(Long postId, Set<Tag> tags) {
        tagRepo.batchUpdateByPostId(postId, tags);
    }
}
