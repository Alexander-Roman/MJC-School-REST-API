package com.epam.esm.persistence.specification.certificate;

import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.persistence.specification.Specification;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TagNameSpecification implements Specification<Certificate> {

    private static final String SQL_FIND_BY_TAG_NAME = "\n" +
            "SELECT certificate_by_tag_name.certificate_id, \n" +
            "       certificate_by_tag_name.certificate_name, \n" +
            "       certificate_by_tag_name.description, \n" +
            "       certificate_by_tag_name.price, \n" +
            "       certificate_by_tag_name.duration, \n" +
            "       certificate_by_tag_name.create_date, \n" +
            "       certificate_by_tag_name.last_update_date, \n" +
            "       tag.id                                   AS tag_id, \n" +
            "       tag.name                                 AS tag_name \n" +
            "FROM ( \n" +
            "         SELECT certificate.id   AS certificate_id, \n" +
            "                certificate.name AS certificate_name, \n" +
            "                certificate.description, \n" +
            "                certificate.price, \n" +
            "                certificate.duration, \n" +
            "                certificate.create_date, \n" +
            "                certificate.last_update_date \n" +
            "         FROM certificate \n" +
            "                  LEFT JOIN certificate_tag ON certificate.id = certificate_tag.certificate_id \n" +
            "                  LEFT JOIN tag ON certificate_tag.tag_id = tag.id \n" +
            "         WHERE tag.name = :tagName \n" +
            "     ) AS certificate_by_tag_name \n" +
            "         LEFT JOIN certificate_tag ON certificate_by_tag_name.certificate_id = certificate_tag.certificate_id \n" +
            "         LEFT JOIN tag ON certificate_tag.tag_id = tag.id \n";

    private final String tagName;

    public TagNameSpecification(String tagName) {
        this.tagName = tagName;
    }

    @Override
    public String getSubQuery() {
        return SQL_FIND_BY_TAG_NAME;
    }

    @Override
    public Map<String, Object> getParameters() {
        Map<String, Object> namedParameters = new HashMap<>();
        namedParameters.put("tagName", tagName);
        return Collections.unmodifiableMap(namedParameters);
    }

}
