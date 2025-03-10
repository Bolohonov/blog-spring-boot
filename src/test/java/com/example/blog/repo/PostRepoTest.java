package com.example.blog.repo;

import com.example.blog.model.Post;
import com.example.blog.repo.impl.jdbc.PostRepoImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Objects;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class PostRepoTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepoImpl postRepo;

    @BeforeEach
    public void setUp() {
        // Очистка базы данных перед каждым тестом
        postRepo.deleteAll();
    }

    @Test
    public Long saveWithoutTags_success {


    }
}
