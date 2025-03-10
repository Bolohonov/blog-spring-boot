package com.example.blog.repo;

import com.example.blog.config.TestConfig;
import com.example.blog.model.Post;
import com.example.blog.repo.impl.jdbc.PostRepoImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class PostRepoTest {

    @Autowired
    private PostRepoImpl postRepo;

    @BeforeEach
    public void setUp() {
        // Очистка базы данных перед каждым тестом
//        postRepo.deleteAll();
    }

    @Test
    @Sql({
            "sql/posts.sql",
            "sql/tags.sql",
            "sql/comments.sql",
            "sql/likes.sql",
            "sql/posts_tags.sql",
    })
    void getById_success() {
        Post post = postRepo.getById(2L);
        assertEquals(2L, post.getId());


    }
}
