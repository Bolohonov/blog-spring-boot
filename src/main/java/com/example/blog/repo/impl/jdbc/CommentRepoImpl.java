package com.example.blog.repo.impl.jdbc;

import lombok.RequiredArgsConstructor;
import com.example.blog.model.Comment;
import com.example.blog.repo.CommentRepo;
import org.example.blog.utils.SqlUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommentRepoImpl implements CommentRepo {
    private static final String SAVE_COMMENT = "INSERT INTO COMMENTS (post_id, content) VALUES (?, ?)";
    private static final String DELETE_COMMENT = "DELETE FROM COMMENTS WHERE id = ?";
    private static final String FIND_BY_POST_ID = """
            SELECT C.*
            FROM COMMENTS C
            WHERE C.post_id IN (%s)
            """;

    private final JdbcTemplate template;

    public CommentRepoImpl(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public void save(Comment comment) {
        template.update(
                SAVE_COMMENT,
                comment.getPostId(),
                comment.getText()
        );
    }

    @Override
    public void delete(Long id) {
        template.update(DELETE_COMMENT, id);
    }

    @Override
    public List<Comment> findByPostIds(List<Long> postIds) {
        String inSql = String.join(",", Collections.nCopies(postIds.size(), "?"));
        return template.query(
                String.format(FIND_BY_POST_ID, inSql),
                this::mapToComment,
                postIds.toArray()
        );
    }

    private Comment mapToComment(ResultSet rs, int rowNumber) throws SQLException {
        return Comment.builder()
                .id(SqlUtils.getLong(rs, "id"))
                .postId(SqlUtils.getLong(rs, "post_id"))
                .text(SqlUtils.getStringOrElseEmpty(rs, "content"))
                .created(SqlUtils.getLocalDateTime(rs, "created"))
                .created(SqlUtils.getLocalDateTime(rs, "updated"))
                .build();
    }
}
