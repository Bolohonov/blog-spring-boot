package com.example.blog.repo.impl.jdbc;

import lombok.RequiredArgsConstructor;
import com.example.blog.model.Post;
import com.example.blog.model.Tag;
import com.example.blog.repo.TagRepo;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class TagRepoImpl implements TagRepo {
    private static final int MAX_BATCH_SIZE = 500;

    private static final String SAVE_POST_TAGS = """
            INSERT INTO posts_tags (post_id, tag_id) VALUES (?, ?)
            """;

    private static final String SAVE_TAGS = """ 
    INSERT INTO TAGS (name) VALUES (?)
    """;

    private static final String FIND_BY_NAME_QUERY = """
            SELECT T.*
            FROM TAGS T
            WHERE LOWER(T.name) IN (%s)
            """;

    private static final String FIND_BY_POST_IDS = """
            SELECT t.*, pt.post_id as post_id
            FROM tags t
            JOIN posts_tags pt ON pt.tag_id = t.id
            WHERE pt.post_id IN (%s)
            """;

    private final JdbcTemplate template;

    public TagRepoImpl(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public List<Tag> save(List<Tag> tags) {
        for (Tag tag : tags) {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            try {
                template.update(
                        connection -> {
                            PreparedStatement ps = connection.prepareStatement(SAVE_TAGS,
                                    Statement.RETURN_GENERATED_KEYS);
                            ps.setString(1, tag.getName());
                            return ps;
                        },
                        keyHolder
                );
                Long tagId = (long) Objects.requireNonNull(keyHolder.getKeys()).get("id");
                tag.setId(tagId);

            } catch (EmptyResultDataAccessException exp) {
                return null;
            } catch (DuplicateKeyException e) {
            }
        }
        return tags;
    }

    @Override
    public void batchUpdateByPostId(Long postId, Set<Tag> tags) {
        if (!CollectionUtils.isEmpty(tags)) {
            final List<Long> tagIds = tags.stream().map(Tag::getId).toList();
            List<Long> ids = new ArrayList<>(tagIds).stream().filter(Objects::nonNull).toList();
            int i = 1;
            while (i != tagIds.size() / MAX_BATCH_SIZE + 2) {
                List<Long> finalIds = ids;
                if (!CollectionUtils.isEmpty(finalIds)) {
                    template.batchUpdate(
                            SAVE_POST_TAGS,
                            new BatchPreparedStatementSetter() {
                                public void setValues(PreparedStatement ps, int i) throws SQLException {
                                    ps.setLong(1, postId);
                                    ps.setLong(2, finalIds.get(i));
                                }

                                public int getBatchSize() {
                                    return finalIds.size();
                                }
                            });
                }
                if (ids.size() > MAX_BATCH_SIZE) {
                    ids = ids.subList(0, MAX_BATCH_SIZE);
                }
                i++;
            }
        }
    }

    @Override
    public List<Tag> getOrCreate(Set<String> tagNames) {
        List<Tag> existedTags = this.findByNameInIgnoreCase(tagNames);
        Set<String> existedTagNames = existedTags.stream().map(Tag::getName).collect(Collectors.toSet());
        List<Tag> newTags = new ArrayList<>();
        tagNames.forEach(t -> {
            if (!existedTagNames.contains(t)) {
                Tag tag = new Tag();
                tag.setName(t);
                newTags.add(tag);
            }
        });
        this.save(newTags);
        newTags.addAll(existedTags);
        return newTags;
    }

    @Override
    public List<Tag> findAll() {
        return template.query(
                "select id, name from tags",
                (rs, rowNum) -> new Tag(
                        rs.getLong("id"),
                        rs.getString("name")
                )
        );
    }

    @Override
    public List<Tag> findByPostIds(List<Long> postIds) {
        String inSql = String.join(",", Collections.nCopies(postIds.size(), "?"));
        return template.query(
                String.format(FIND_BY_POST_IDS, inSql),
                this::mapToTagWithPostId,
                postIds.toArray()
        );
    }

    private List<Tag> findByNameInIgnoreCase(Set<String> tagNames) {
        String inSql = String.join(",", Collections.nCopies(tagNames.size(), "?"));
        List<Tag> tags = template.query(
                String.format(FIND_BY_NAME_QUERY, inSql),
                this::mapToTag,
                tagNames.toArray()
        );
        return tags.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    private Tag mapToTag(ResultSet rs, int rowNumber) throws SQLException {
        if (rs.getString("name") == null) {
            return null;
        }
        return new Tag(rs.getLong("id"), rs.getString("name"));
    }

    private Tag mapToTagWithPostId(ResultSet rs, int rowNumber) throws SQLException {
        if (rs.getString("name") == null) {
            return null;
        }
        return new Tag(rs.getLong("id"), rs.getString("name"), rs.getLong("post_id"));
    }
}
