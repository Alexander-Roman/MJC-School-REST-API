package com.epam.esm.persistence.dao;

import com.epam.esm.persistence.entity.Tag;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class TagDaoImpl implements TagDao {

    public static final String SQL_FIND_BY_NAME = "\n" +
            "SELECT tag.id   AS tag_id, \n" +
            "       tag.name AS tag_name \n" +
            "FROM tag \n" +
            "WHERE tag.name = :tagName \n";
    private static final String SQL_FIND_BY_ID = "\n" +
            "SELECT tag.id   AS tag_id, \n" +
            "       tag.name AS tag_name \n" +
            "FROM tag \n" +
            "WHERE tag.id = :tagId \n";
    private static final String SQL_FIND_ALL = "\n" +
            "SELECT tag.id   AS tag_id, \n" +
            "       tag.name AS tag_name \n" +
            "FROM tag \n";

    private static final String SQL_INSERT = "\n" +
            "INSERT INTO tag (name) VALUES (:name) \n";

    private static final String SQL_DELETE_BY_ID = "\n" +
            "DELETE FROM tag \n" +
            "WHERE id = :id \n";

    private static final String SQL_DELETE_UNUSED = "\n" +
            "DELETE FROM tag \n" +
            "WHERE id NOT IN (SELECT DISTINCT tag_id FROM certificate_tag); \n";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final RowMapper<Tag> tagRowMapper;

    public TagDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                      RowMapper<Tag> tagRowMapper) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.tagRowMapper = tagRowMapper;
    }

    @Override
    public Optional<Tag> findById(Long tagId) {
        Map<String, Object> namedParameters = new HashMap<>();
        namedParameters.put("tagId", tagId);
        List<Tag> results = namedParameterJdbcTemplate.query(SQL_FIND_BY_ID, namedParameters, tagRowMapper);
        return results.stream().findFirst();
    }

    @Override
    public Optional<Tag> findByName(String tagName) {
        Map<String, Object> namedParameters = new HashMap<>();
        namedParameters.put("tagName", tagName);
        List<Tag> results = namedParameterJdbcTemplate.query(SQL_FIND_BY_NAME, namedParameters, tagRowMapper);
        return results.stream().findFirst();
    }

    @Override
    public List<Tag> findAll() {
        return namedParameterJdbcTemplate.query(SQL_FIND_ALL, tagRowMapper);
    }

    @Override
    public Tag create(Tag tag) {
        SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(tag);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(SQL_INSERT, sqlParameterSource, keyHolder, new String[]{"id"});
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
        namedParameterJdbcTemplate.update(SQL_DELETE_BY_ID, namedParameters);
    }

    @Override
    public void deleteUnused() {
        Map<String, Object> namedParameters = Collections.emptyMap();
        namedParameterJdbcTemplate.update(SQL_DELETE_UNUSED, namedParameters);
    }

}
