package com.example.blog.repo;

import com.example.blog.model.Post;
import com.example.blog.repo.impl.jdbc.PostRepoImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@DataJdbcTest
public class PostRepoTest {
    @Mock
    private JdbcTemplate template;

    @InjectMocks
    private PostRepoImpl postRepoImpl;

    @BeforeEach
    public void setUp() {
        postRepoImpl = new PostRepoImpl(template);
    }

    @Test
    void testGetById() {
        Post post = new Post();
        post.setId(1L);
        post.setTitle("Sample Title");
        post.setContent("Sample Content");

        when(template.queryForObject(anyString(), any(RowMapper.class), anyLong())).thenReturn(post);

        Post result = postRepoImpl.getById(1L);

        assertNotNull(result);
        assertEquals("Sample Title", result.getTitle());
        assertEquals("Sample Content", result.getContent());

    }

    @Test
    void testDeleteById() {
        doAnswer(invocation -> null).when(template).update(anyString(), anyLong());

        postRepoImpl.deleteById(1L);

        verify(template, times(1)).update(anyString(), anyLong());
    }

    @Test
    void testUpdateWithoutTags() {
        Post post = new Post();
        post.setId(1L);
        post.setTitle("Updated Title");
        post.setContent("Updated Content");
        post.setImage(new byte[]{1, 2, 3});

        doAnswer(invocation -> null).when(template).update(anyString(), anyString(), anyString(), any(byte[].class), anyLong());

        postRepoImpl.updateWithoutTags(post);

        verify(template, times(1)).update(anyString(), anyString(), anyString(), any(byte[].class), anyLong());
    }

    @Test
    void testFindAll() {
        Pageable pageable = PageRequest.of(0, 10);

        List<Post> posts = List.of(new Post());
        when(template.query(anyString(), any(RowMapper.class), anyInt(), anyInt())).thenReturn(posts);
        when(template.queryForObject(anyString(), any(Class.class))).thenReturn(1);

        Page<Post> result = postRepoImpl.findAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(posts, result.getContent());
    }

    @Test
    void testSaveWithoutTags() throws SQLException {
        Post post = new Post();
        post.setTitle("Sample Title");
        post.setContent("Sample Content");
        post.setImage(new byte[]{1, 2, 3});

        KeyHolder keyHolder = new GeneratedKeyHolder();

        doAnswer(invocation -> {
            PreparedStatementCreator psc = invocation.getArgument(0);
            Connection connection = mock(Connection.class);
            PreparedStatement ps = connection.prepareStatement("INSERT INTO POSTS (title, content, image) VALUES (?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, post.getTitle());
            ps.setString(2, post.getContent());
            ps.setBytes(3, post.getImage());
            ps.executeUpdate();
            keyHolder.getKeyList().add(Collections.singletonMap("id", 1L));
            return null;
        }).when(template).update(any(PreparedStatementCreator.class), any(KeyHolder.class));

        Long postId = postRepoImpl.saveWithoutTags(post);

        assertNotNull(postId);
        assertEquals(1L, postId);
    }
}
