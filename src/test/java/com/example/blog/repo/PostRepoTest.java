package com.example.blog.repo;

import com.example.blog.model.Post;
import com.example.blog.repo.impl.jdbc.PostRepoImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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

    private final RowMapper<Post> rowMapper = (rs, rowNum) -> {
        return new Post(
                rs.getLong("id"),
                rs.getString("title"),
                rs.getString("content"),
                rs.getBytes("image"),
                rs.getTimestamp("created").toLocalDateTime(),
                rs.getTimestamp("updated").toLocalDateTime()
        );
    };

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
}
