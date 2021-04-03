package com.epam.esm.persistence.query.certificatetag;

import com.epam.esm.persistence.entity.CertificateTag;
import com.epam.esm.persistence.query.UpdateQuery;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DeleteByCertificateIdQuery implements UpdateQuery<CertificateTag> {

    private static final String SQL_DELETE_BY_CERTIFICATE_ID_QUERY = "\n" +
            "DELETE FROM certificate_tag \n" +
            "WHERE certificate_id = :certificateId \n";

    private final Long certificateId;

    public DeleteByCertificateIdQuery(Long certificateId) {
        this.certificateId = certificateId;
    }

    @Override
    public String getSql() {
        return SQL_DELETE_BY_CERTIFICATE_ID_QUERY;
    }

    @Override
    public Map<String, Object> getParameters() {
        Map<String, Object> namedParameters = new HashMap<>();
        namedParameters.put("certificateId", certificateId);
        return Collections.unmodifiableMap(namedParameters);
    }
}
