package com.example.blog.controller;

import com.example.blog.controller.request.PostRequest;
import com.example.blog.controller.response.PostResponse;
import com.example.blog.model.Post;
import com.example.blog.service.PostService;
import com.example.blog.service.TagService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class PostControllerTest {

    private static final Long EXISTING_POST_ID = 2L;

    private final Post post = getPost();
    private final PostResponse postResponse = getPostResponse();
    private final PostRequest postRequest = getPostRequest();


    private MockMvc mockMvc;

    @Mock
    private PostService postService;
    @Mock
    private TagService tagService;
    @InjectMocks
    private PostController postController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(postController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void savePost_shouldSavePost() throws Exception {
        mockMvc.perform(post("/blog/post")
                        .param("title", "title")
                        .param("content", "content")
                        .param("tags", "tag1,tag2,tag3")
                        .content(new byte[]{1, 2, 3})
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                )
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void getPost_shouldReturnHtmlWithPost() throws Exception {
        when(postService.getById(EXISTING_POST_ID)).thenReturn(postResponse);
        mockMvc.perform(get("/blog/post/2"))
                .andExpect(status().isOk())
                .andExpect(view().name("post"))
                .andExpect(model().attributeExists("post"))
                .andExpect(model().attribute("post", hasProperty("title", is("Пост1"))));;
    }

    @Test
    void getPost_shouldReturnHtmlWithPosts() throws Exception {
        List<PostResponse> responses = List.of(postResponse);
        Page<PostResponse> posts = new PageImpl<>(responses);
        when(postService.getPosts(ArgumentMatchers.any(Pageable.class))).thenReturn(posts);
        when(tagService.getAllTags()).thenReturn(List.of("Tag1", "Tag2"));

        mockMvc.perform(get("/blog")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(view().name("feed"))
                .andExpect(model().attributeExists("posts"))
                .andExpect(model().attribute("posts", responses));

    }

    @Test
    void deletePost_shouldDeletePost() throws Exception {
        mockMvc.perform(delete("/blog/post/3"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void updatePost_shouldUpdatePost() throws Exception {
        mockMvc.perform(put("/blog/post/4/update")
                        .param("title", "newTitle")
                        .param("content", "newContent")
                        .param("tags", "Тег1,Тег3")
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                )
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void addComment_shouldAddComment() throws Exception {
        mockMvc.perform(post("/blog/post/2/comment")
                        .param("text", "newText")
                )
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void deleteComment_shouldDeleteComment() throws Exception {
        mockMvc.perform(delete("/blog/post/2/comment/1"))
                .andExpect(status().isOk());
    }

    @Test
    void likePost_shouldLikePost() throws Exception {
        mockMvc.perform(get("/blog/post/2/like"))
                .andExpect(status().isOk());
    }

    Post getPost() {
        return new Post(2L, "Пост1", "11111", null, LocalDateTime.now(), LocalDateTime.now());
    }

    PostResponse getPostResponse() {
        return PostResponse.builder()
                .id(2L)
                .title("Пост1")
                .content("11111")
                .build();
    }

    PostRequest getPostRequest() {
        PostRequest postRequest = new PostRequest();
        postRequest.setTitle("Пост1");
        return postRequest;
    }

}
