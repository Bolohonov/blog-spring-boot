package com.example.blog.repo.impl.jdbc;

import lombok.RequiredArgsConstructor;
import com.example.blog.model.Post;
import com.example.blog.repo.PostRepo;
import org.example.blog.utils.SqlUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class PostRepoImpl implements PostRepo {
    private static final String SAVE_QUERY = """
            INSERT INTO POSTS (title, content, image) VALUES (?, ?, ?)
            """;
    private static final String COUNT_POSTS = "SELECT COUNT(DISTINCT P.id) FROM POSTS P";
    private static final String FIND_ALL = """
            SELECT DISTINCT P.*
            FROM POSTS P
            LEFT JOIN POSTS_TAGS PT ON P.id = PT.post_id
            LEFT JOIN TAGS T ON PT.tag_id = T.id
            LIMIT ? OFFSET ?
            """;

    private static final String UPDATE_QUERY = """ 
            UPDATE posts SET title = ?, content = ?, image = ? WHERE id = ?
            """;
    private static final String FIND_BY_ID = """
            SELECT P.*
            FROM POSTS P
            WHERE P.id = ?
            """;

    private static final String DELETE_BY_ID = "DELETE FROM POSTS P WHERE P.id = ?";

    private final JdbcTemplate template;

    public PostRepoImpl(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public Long saveWithoutTags(Post post) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            template.update(
                    connection -> {
                        PreparedStatement ps = connection.prepareStatement(SAVE_QUERY, Statement.RETURN_GENERATED_KEYS);
                        ps.setString(1, post.getTitle());
                        ps.setString(2, post.getContent());
                        ps.setBytes(3, post.getImage());
                        return ps;
                    },
                    keyHolder
            );
        } catch (EmptyResultDataAccessException exp) {
            return null;
        }
        return (Long) Objects.requireNonNull(keyHolder.getKeys()).get("id");
    }

    @Override
    public Post getById(Long id) {
        return template.queryForObject(
                FIND_BY_ID,
                this::mapToPost,
                id
        );
    }

    @Override
    public void deleteById(Long id) {
        template.update(DELETE_BY_ID, id);
    }

    @Override
    public void updateWithoutTags(Post post) {
        System.out.println("UPDATE");
        template.update(
                UPDATE_QUERY, post.getTitle(), post.getContent(), post.getImage(), post.getId()
        );
    }

    @Override
    public Page<Post> findAll(Pageable pageable) {
        int limit = pageable.getPageSize();
        int offset = pageable.getPageNumber() * limit;

        List<Post> posts = template.query(
                FIND_ALL,
                this::mapToPost,
                limit,
                offset
        );
        Integer size;
        try {
            size = template.queryForObject(COUNT_POSTS, Integer.class);
        } catch (EmptyResultDataAccessException exp) {
            size = 0;
        }
        return new PageImpl<>(posts, pageable, size);
    }

    Post mapToPost(ResultSet rs, int rowNumber) throws SQLException {
        return new Post(
                SqlUtils.getLong(rs, "id"),
                SqlUtils.getStringOrElseEmpty(rs, "title"),
                SqlUtils.getStringOrElseEmpty(rs, "content"),
                rs.getBytes("image"),
                SqlUtils.getLocalDateTime(rs, "created"),
                SqlUtils.getLocalDateTime(rs, "updated")
        );
    }
}
