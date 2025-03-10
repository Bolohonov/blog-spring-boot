package com.example.blog.controller;

import com.example.blog.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
@ActiveProfiles("test")
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private PostService postService;

    @Test
    @Sql({
            "sql/posts.sql",
            "sql/tags.sql",
            "sql/comments.sql",
            "sql/likes.sql",
            "sql/posts_tags.sql",
    })
    void getPosts_shouldReturnHtmlWithPosts() throws Exception {
        mockMvc.perform(get("/api/blog"))
                .andExpect(status().isOk());
    }

    @Test
    @Sql({
            "sql/posts.sql",
            "sql/tags.sql"
    })
    void savePost_shouldSavePost() throws Exception {
        mockMvc.perform(post("/api/blog/post")
                        .param("title", "title")
                        .param("content", "content")
                        .param("tags", "tag1,tag2,tag3")
                        .content(new byte[]{1, 2, 3})
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                )
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @Sql({"sql/posts.sql",
            "sql/tags.sql",
            "sql/posts_tags.sql",
            "sql/comments.sql",
            "sql/likes.sql"})
    void getPost_shouldReturnHtmlWithPost() throws Exception {
        mockMvc.perform(get("/api/blog/post/2"))
                .andExpect(status().isOk());
    }

    @Test
    @Sql("sql/posts.sql")
    void deletePost_shouldDeletePost() throws Exception {
        mockMvc.perform(delete("/api/blog/post/3"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @Sql({"sql/posts.sql",
            "sql/tags.sql"})
    void updatePost_shouldUpdatePost() throws Exception {
        mockMvc.perform(put("/api/blog/post/4/update")
                        .param("title", "newTitle")
                        .param("content", "newContent")
                        .param("tags", "Тег1,Тег3")
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                )
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @Sql("sql/posts.sql")
    void addComment_shouldAddComment() throws Exception {
        mockMvc.perform(post("/api/blog/post/2/comment")
                        .param("text", "newText")
                )
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @Sql({"sql/posts.sql", "sql/comments.sql"})
    void deleteComment_shouldDeleteComment() throws Exception {
        mockMvc.perform(delete("/api/blog/post/2/comment/1"))
                .andExpect(status().isOk());
    }

    @Test
    @Sql("sql/posts.sql")
    void likePost_shouldLikePost() throws Exception {
        mockMvc.perform(get("/api/blog/post/2/like"))
                .andExpect(status().isOk());
    }

}
