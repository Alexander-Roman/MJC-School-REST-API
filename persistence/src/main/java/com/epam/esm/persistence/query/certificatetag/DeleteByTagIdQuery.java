package com.epam.esm.persistence.query.certificatetag;

import com.epam.esm.persistence.entity.CertificateTag;
import com.epam.esm.persistence.query.UpdateQuery;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DeleteByTagIdQuery implements UpdateQuery<CertificateTag> {

    private static final String SQL_DELETE_BY_TAG_ID_QUERY = "\n" +
            "DELETE FROM certificate_tag \n" +
            "WHERE tag_id = :tagId \n";

    private final Long tagId;

    public DeleteByTagIdQuery(Long tagId) {
        this.tagId = tagId;
    }

    @Override
    public String getSql() {
        return SQL_DELETE_BY_TAG_ID_QUERY;
    }

    @Override
    public Map<String, Object> getParameters() {
        Map<String, Object> namedParameters = new HashMap<>();
        namedParameters.put("tagId", tagId);
        return Collections.unmodifiableMap(namedParameters);
    }

}
