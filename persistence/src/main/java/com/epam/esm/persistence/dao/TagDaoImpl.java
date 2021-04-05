package com.epam.esm.persistence.dao;

import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.persistence.exception.PersistenceException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class TagDaoImpl implements TagDao {

    private static final String SQL_FIND_BY_ID = "\n" +
            "SELECT tag.id   AS tag_id, \n" +
            "       tag.name AS tag_name \n" +
            "FROM tag \n" +
            "WHERE tag.id = :tagId \n";

    private static final String SQL_FIND_ALL = "\n" +
            "SELECT tag.id   AS tag_id, \n" +
            "       tag.name AS tag_name \n" +
            "FROM tag \n";

    private static final String SQL_INSERT = "INSERT INTO tag (name) VALUES (:name) \n";

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
    public List<Tag> findAll() {
        return namedParameterJdbcTemplate.query(SQL_FIND_ALL, tagRowMapper);
    }

    @Override
    public Tag create(Tag tag) {
        SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(tag);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(SQL_INSERT, sqlParameterSource, keyHolder);

        List<Map<String, Object>> keyList = keyHolder.getKeyList();
        Long id = (Long) keyList
                .stream()
                .findFirst()
                .orElseThrow(() -> new PersistenceException("Unexpected query result!"))
                .values()
                .stream()
                .findFirst()
                .orElseThrow(() -> new PersistenceException("Unexpected query result!"));
        return Tag.Builder
                .from(tag)
                .setId(id)
                .build();
    }

    @Override
    public void createIfNotExists(Set<Tag> tags) {
        List<String> parameterNames = new ArrayList<>();
        Map<String, Object> namedParameters = new HashMap<>();
        int count = 0;
        for (Tag tag : tags) {
            ++count;

            String parameterName = ":tagName" + count;
            parameterNames.add(parameterName);

            String tagName = tag.getName();
            String key = "tagName" + count;
            namedParameters.put(key, tagName);
        }
        String valueSet = parameterNames
                .stream()
                .map(param -> String.format("(%s)", param))
                .collect(Collectors.joining(", "));
        String sql = String.format("INSERT INTO tag (name) VALUES %s ON CONFLICT (name) DO NOTHING", valueSet);
        namedParameterJdbcTemplate.update(sql, namedParameters);
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
