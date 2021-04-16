package com.epam.esm.persistence.dao;

import com.epam.esm.persistence.entity.Tag;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class TagDaoImpl implements TagDao {

    private static final String SQL_FIND_ALL = "\n" +
            "SELECT tag.id   AS tag_id, \n" +
            "       tag.name AS tag_name \n" +
            "FROM tag \n";

    private static final String SQL_FIND_BY_NAME = "\n" +
            SQL_FIND_ALL +
            "WHERE tag.name = LOWER(:tagName) \n";

    private static final String SQL_FIND_BY_ID = "\n" +
            SQL_FIND_ALL +
            "WHERE tag.id = :tagId \n";

    private static final String SQL_INSERT = "\n" +
            "INSERT INTO tag (name) VALUES (LOWER(:name)) \n";

    private static final String SQL_DELETE_BY_ID = "\n" +
            "DELETE FROM tag \n" +
            "WHERE id = :id \n";

    private static final String SQL_DELETE_UNUSED = "\n" +
            "DELETE FROM tag \n" +
            "WHERE id NOT IN (SELECT DISTINCT tag_id FROM certificate_tag); \n";

    private final NamedParameterJdbcOperations jdbcOperations;
    private final RowMapper<Tag> tagRowMapper;

    public TagDaoImpl(NamedParameterJdbcOperations jdbcOperations,
                      RowMapper<Tag> tagRowMapper) {
        this.jdbcOperations = jdbcOperations;
        this.tagRowMapper = tagRowMapper;
    }

    @Override
    public Optional<Tag> findById(Long tagId) {
        Map<String, Object> namedParameters = new HashMap<>();
        namedParameters.put("tagId", tagId);
        List<Tag> results = jdbcOperations.query(SQL_FIND_BY_ID, namedParameters, tagRowMapper);
        return results.stream().findFirst();
    }

    @Override
    public Optional<Tag> findByName(String tagName) {
        Map<String, Object> namedParameters = new HashMap<>();
        namedParameters.put("tagName", tagName);
        List<Tag> results = jdbcOperations.query(SQL_FIND_BY_NAME, namedParameters, tagRowMapper);
        return results.stream().findFirst();
    }

    @Override
    public List<Tag> findAll() {
        return jdbcOperations.query(SQL_FIND_ALL, tagRowMapper);
    }

    @Override
    public Tag create(Tag tag) {
        SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(tag);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcOperations.update(SQL_INSERT, sqlParameterSource, keyHolder, new String[]{"id"});
        Long id = keyHolder.getKeyAs(Long.class);
        return Tag.Builder
                .from(tag)
                .setId(id)
                .build();
    }

    @Override
    public void delete(Long id) {
        Map<String, Object> namedParameters = new HashMap<>();
        namedParameters.put("id", id);
        jdbcOperations.update(SQL_DELETE_BY_ID, namedParameters);
    }

    @Override
    public void deleteUnused() {
        Map<String, Object> namedParameters = Collections.emptyMap();
        jdbcOperations.update(SQL_DELETE_UNUSED, namedParameters);
    }

}
