package com.example.blog.repo.impl.jdbc;

import lombok.RequiredArgsConstructor;
import com.example.blog.model.Like;
import com.example.blog.repo.LikeRepo;
import com.example.blog.utils.SqlUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class LikeRepoImpl implements LikeRepo {
    private static final String SAVE_LIKE = "INSERT INTO LIKES (post_id, user_id) VALUES (?, ?)";
    private static final String FIND_BY_POST_ID = """
            SELECT l.*
            FROM likes l
            WHERE l.post_id IN (%s)
            """;

    private final JdbcTemplate template;

    @Override
    public void save(Like like) {
        template.update(
                SAVE_LIKE,
                like.getPostId(),
                1
        );
    }

    @Override
    public List<Like> findByPostIds(List<Long> postIds) {
        String inSql = String.join(",", Collections.nCopies(postIds.size(), "?"));
        return template.query(
                String.format(FIND_BY_POST_ID, inSql),
                this::mapToLike,
                postIds.toArray()
        );
    }

    private Like mapToLike(ResultSet rs, int rowNumber) throws SQLException {
        return new Like(
                SqlUtils.getLong(rs, "id"),
                SqlUtils.getLong(rs, "post_id"),
                SqlUtils.getLong(rs, "user_id"),
                SqlUtils.getLocalDateTime(rs, "created")
        );
    }
}
