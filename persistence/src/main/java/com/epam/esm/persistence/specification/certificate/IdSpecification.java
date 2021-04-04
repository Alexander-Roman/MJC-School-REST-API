package com.epam.esm.persistence.specification.certificate;

import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.persistence.specification.Specification;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class IdSpecification implements Specification<Certificate> {

    private static final String SQL_FIND_BY_ID = "\n" +
            "SELECT certificate.id AS certificate_id, \n" +
            "       certificate.name AS certificate_name, \n" +
            "       certificate.description, \n" +
            "       certificate.price, \n" +
            "       certificate.duration, \n" +
            "       certificate.create_date, \n" +
            "       certificate.last_update_date, \n" +
            "       tag.id AS tag_id, \n" +
            "       tag.name AS tag_name \n" +
            "FROM certificate \n" +
            "         LEFT JOIN certificate_tag ON certificate.id = certificate_tag.certificate_id \n" +
            "         LEFT JOIN tag ON certificate_tag.tag_id = tag.id \n" +
            "WHERE certificate.id = :certificateId \n";

    private final Long certificateId;

    public IdSpecification(Long certificateId) {
        this.certificateId = certificateId;
    }

    @Override
    public String getSubQuery() {
        return SQL_FIND_BY_ID;
    }

    @Override
    public Map<String, Object> getParameters() {
        Map<String, Object> namedParameters = new HashMap<>();
        namedParameters.put("certificateId", certificateId);
        return Collections.unmodifiableMap(namedParameters);
    }

}
