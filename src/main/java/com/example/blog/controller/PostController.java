package com.example.blog.controller;

import lombok.RequiredArgsConstructor;
import com.example.blog.controller.request.CommentRequest;
import com.example.blog.controller.request.PostRequest;
import com.example.blog.controller.response.PostResponse;
import org.example.blog.service.PostService;
import org.example.blog.service.TagService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/blog")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final TagService tagService;

    @PostMapping(path = "/post", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public String savePost(@ModelAttribute PostRequest request) {
        postService.save(request);
        return "redirect:/api/blog";
    }

    @GetMapping("/post/{id}")
    public String getPost(@PathVariable("id") Long id, Model model) {
        PostResponse post = postService.getById(id);
        model.addAttribute("post", post);
        return "post";
    }

    @DeleteMapping( "/post/{id}")
    public String deletePost(@PathVariable("id") Long id) {
        postService.removeById(id);
        return "redirect:/api/blog";
    }

    @PutMapping( "/post/{id}/update")
    public String update(@ModelAttribute PostRequest request, @PathVariable("id") Long id) {
        postService.update(id, request);
        return "redirect:/api/blog";
    }

    @PostMapping( "/post/{id}/comment")
    public String addComment(@PathVariable("id") Long id, @ModelAttribute CommentRequest comment) {
        postService.addComment(id, comment);
        return "redirect:/api/blog/post/" + id;
    }

    @DeleteMapping( "/post/{id}/comment/{commentId}")
    public void deleteComment(@PathVariable("id") Long id, @PathVariable("commentId") Long commentId) {
        postService.deleteComment(id, commentId);
    }

    @GetMapping( "/post/{id}/like")
    public void likePost(@PathVariable("id") Long id) {
        postService.like(id);
    }

    @GetMapping
    public String getPosts(@PageableDefault Pageable pageable, Model model) {
        List<String> tags = tagService.getAllTags();
        Page<PostResponse> posts = postService.getPosts(pageable);
        model.addAttribute("posts", posts.getContent());
        model.addAttribute("currentPage", posts.getNumber() + 1);
        model.addAttribute("totalPages", posts.getTotalPages());
        model.addAttribute("tags", tags);
        return "feed";
    }
}
