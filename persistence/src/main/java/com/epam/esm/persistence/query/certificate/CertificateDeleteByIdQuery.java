package com.epam.esm.persistence.query.certificate;

import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.persistence.query.UpdateQuery;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CertificateDeleteByIdQuery implements UpdateQuery<Certificate> {

    private static final String SQL_DELETE_BY_ID = "\n" +
            "DELETE FROM certificate \n" +
            "WHERE id = :id \n";

    private final Long id;

    public CertificateDeleteByIdQuery(Long id) {
        this.id = id;
    }

    @Override
    public String getSql() {
        return SQL_DELETE_BY_ID;
    }

    @Override
    public Map<String, Object> getParameters() {
        Map<String, Object> namedParameters = new HashMap<>();
        namedParameters.put("id", id);
        return Collections.unmodifiableMap(namedParameters);
    }

}
