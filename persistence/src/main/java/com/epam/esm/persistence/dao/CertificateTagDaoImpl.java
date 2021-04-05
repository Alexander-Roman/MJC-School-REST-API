package com.epam.esm.persistence.dao;

import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.persistence.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class CertificateTagDaoImpl implements CertificateTagDao {

    private static final String SQL_DELETE_BY_CERTIFICATE_ID_QUERY = "\n" +
            "DELETE FROM certificate_tag \n" +
            "WHERE certificate_id = :certificateId \n";

    private static final String SQL_DELETE_BY_TAG_ID_QUERY = "\n" +
            "DELETE FROM certificate_tag \n" +
            "WHERE tag_id = :tagId \n";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public CertificateTagDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public void resolveAddedTagsByNames(Certificate certificate) {
        Set<Tag> tags = certificate.getTags();

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
                .map(param -> String.format("(:certificateId, (SELECT tag.id FROM tag WHERE tag.name = %s))", param))
                .collect(Collectors.joining(", "));

        String templateSqlAddTagsByNames = "\n" +
                "INSERT INTO certificate_tag (certificate_id, tag_id) \n" +
                "VALUES %s ON CONFLICT (certificate_id, tag_id) DO NOTHING \n";
        String sql = String.format(templateSqlAddTagsByNames, valueSet);

        Long certificateId = certificate.getId();
        namedParameters.put("certificateId", certificateId);

        namedParameterJdbcTemplate.update(sql, namedParameters);
    }

    @Override
    public void resolveRemovedTagsByNames(Certificate certificate) {
        Set<Tag> tags = certificate.getTags();

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
        String values = String.join(", ", parameterNames);
        String templateSqlRemoveUnnecessaryTags = "\n" +
                "DELETE FROM certificate_tag \n" +
                "WHERE certificate_id = :certificateId \n" +
                "  AND tag_id NOT IN (SELECT tag.id FROM tag WHERE tag.name IN (%s)) \n";
        String sql = String.format(templateSqlRemoveUnnecessaryTags, values);

        Long certificateId = certificate.getId();
        namedParameters.put("certificateId", certificateId);

        namedParameterJdbcTemplate.update(sql, namedParameters);
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
