package com.example.blog.service;

import lombok.RequiredArgsConstructor;
import com.example.blog.mapper.PostMapper;
import com.example.blog.controller.request.CommentRequest;
import com.example.blog.controller.request.PostRequest;
import com.example.blog.controller.response.PostResponse;
import com.example.blog.model.Comment;
import com.example.blog.model.Like;
import com.example.blog.model.Post;
import com.example.blog.model.Tag;
import com.example.blog.repo.PostRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepo postRepo;
    private final PostMapper postMapper;
    private final CommentService commentService;
    private final LikeService likeService;
    private final TagService tagService;

    @Transactional
    public void save(PostRequest request) {
        Post post = postMapper.toPost(request);
        Long postId = postRepo.saveWithoutTags(post);
        tagService.batchUpdateByPostId(postId, post.getTags());
    }

    public PostResponse getById(Long id) {
        return postMapper.toResponse(postRepo.getById(id));
    }

    public void removeById(Long id) {
        postRepo.deleteById(id);
    }

    @Transactional
    public void update(Long id, PostRequest request) {
        Post post = postRepo.getById(id);
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setTags(tagService.save(request.getTags()));
        postRepo.updateWithoutTags(post);
        tagService.batchUpdateByPostId(post.getId(), post.getTags());
    }

    public void addComment(Long id, CommentRequest comment) {
        commentService.save(comment, postRepo.getById(id));
    }

    @Transactional
    public void deleteComment(Long id, Long commentId) {
        Post post = postRepo.getById(id);
        if (!Objects.equals(post, null) && !CollectionUtils.isEmpty(post.getComments())) {
            post.getComments().stream()
                    .filter(comment -> comment.getId().equals(commentId))
                    .findFirst()
                    .ifPresent(commentService::remove);
        }
    }

    public void like(Long id) {
        likeService.addLike(postRepo.getById(id));
    }

    @Transactional(readOnly = true)
    public Page<PostResponse> getPosts(Pageable pageable) {
        Page<Post> posts = postRepo.findAll(pageable);
        List<Long> postIds = posts.stream().map(Post::getId).toList();
        Map<Long, List<Tag>> tags = tagService.getByPostIds(postIds);
        Map<Long, List<Comment>> comments = commentService.getByPostIds(postIds);
        Map<Long, List<Like>> likes = likeService.getByPostIds(postIds);
        posts.forEach(post -> {
            post.getTags().addAll(tags.getOrDefault(post.getId(), Collections.emptyList()));
            post.getComments().addAll(comments.getOrDefault(post.getId(), Collections.emptyList()));
            post.getLikes().addAll(likes.getOrDefault(post.getId(), Collections.emptyList()));
        });
        return posts.map(postMapper::toResponse);
    }


}
