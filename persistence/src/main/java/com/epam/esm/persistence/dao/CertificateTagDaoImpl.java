package com.epam.esm.persistence.dao;

import com.epam.esm.persistence.entity.CertificateTag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class CertificateTagDaoImpl implements CertificateTagDao {

    public static final String SQL_DELETE_BY_ID = "\n" +
            "DELETE FROM certificate_tag WHERE id = :id \n";

    private static final String SQL_FIND_BY_CERTIFICATE_ID = "\n" +
            "SELECT certificate_tag.id, \n" +
            "       certificate_tag.certificate_id, \n" +
            "       certificate_tag.tag_id \n" +
            "FROM certificate_tag \n" +
            "WHERE certificate_id = :certificateId \n";

    private static final String SQL_INSERT = "\n" +
            "INSERT INTO certificate_tag (certificate_id, tag_id) \n" +
            "VALUES (:certificateId, :tagId) \n";

    private static final String SQL_DELETE_BY_CERTIFICATE_ID_QUERY = "\n" +
            "DELETE FROM certificate_tag \n" +
            "WHERE certificate_id = :certificateId \n";

    private static final String SQL_DELETE_BY_TAG_ID_QUERY = "\n" +
            "DELETE FROM certificate_tag \n" +
            "WHERE tag_id = :tagId \n";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final RowMapper<CertificateTag> certificateTagRowMapper;

    @Autowired
    public CertificateTagDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                                 RowMapper<CertificateTag> certificateTagRowMapper) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.certificateTagRowMapper = certificateTagRowMapper;
    }

    @Override
    public List<CertificateTag> findByCertificateId(Long certificateId) {
        Map<String, Object> namedParameters = new HashMap<>();
        namedParameters.put("certificateId", certificateId);
        return namedParameterJdbcTemplate.query(SQL_FIND_BY_CERTIFICATE_ID, namedParameters, certificateTagRowMapper);
    }

    @Override
    public CertificateTag create(CertificateTag certificateTag) {
        SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(certificateTag);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(SQL_INSERT, sqlParameterSource, keyHolder, new String[]{"id"});
        Long id = keyHolder.getKeyAs(Long.class);
        return CertificateTag.Builder
                .from(certificateTag)
                .setId(id)
                .build();
    }

    @Override
    public void create(Collection<CertificateTag> certificateTags) {
        int size = certificateTags.size();
        SqlParameterSource[] sqlParameterSourceArray = new SqlParameterSource[size];
        List<SqlParameterSource> sqlParameterSourceList = certificateTags
                .stream()
                .map(BeanPropertySqlParameterSource::new)
                .collect(Collectors.toList());
        sqlParameterSourceList.toArray(sqlParameterSourceArray);
        namedParameterJdbcTemplate.batchUpdate(SQL_INSERT, sqlParameterSourceArray);
    }

    @Override
    public void delete(Collection<Long> certificateTagIds) {
        int size = certificateTagIds.size();
        SqlParameterSource[] sqlParameterSourceArray = new SqlParameterSource[size];
        List<SqlParameterSource> sqlParameterSourceList = certificateTagIds
                .stream()
                .map(id -> new MapSqlParameterSource().addValue("id", id))
                .collect(Collectors.toList());
        sqlParameterSourceList.toArray(sqlParameterSourceArray);
        namedParameterJdbcTemplate.batchUpdate(SQL_DELETE_BY_ID, sqlParameterSourceArray);
    }

    @Override
    public void deleteByCertificateId(Long certificateId) {
        Map<String, Object> namedParameters = new HashMap<>();
        namedParameters.put("certificateId", certificateId);
        namedParameterJdbcTemplate.update(SQL_DELETE_BY_CERTIFICATE_ID_QUERY, namedParameters);
    }

    @Override
    public void deleteByTagId(Long tagId) {
        Map<String, Object> namedParameters = new HashMap<>();
        namedParameters.put("tagId", tagId);
        namedParameterJdbcTemplate.update(SQL_DELETE_BY_TAG_ID_QUERY, namedParameters);
    }

}
