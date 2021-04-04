package com.epam.esm.persistence.query.certificatetag;

import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.persistence.entity.CertificateTag;
import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.persistence.query.UpdateQuery;

import java.util.*;

public class ResolveRemovedTagsByNamesQuery implements UpdateQuery<CertificateTag> {

    private static final String TEMPLATE_SQL_REMOVE_UNNECESSARY_TAGS = "\n" +
            "DELETE FROM certificate_tag \n" +
            "WHERE certificate_id = :certificateId \n" +
            "  AND tag_id NOT IN (SELECT tag.id FROM tag WHERE tag.name IN (%s)) \n";
    private static final String VALUES_DELIMITER = ", ";

    private final Certificate certificate;

    public ResolveRemovedTagsByNamesQuery(Certificate certificate) {
        this.certificate = certificate;
    }

    @Override
    public String getSql() {
        Set<Tag> tags = certificate.getTags();
        List<String> parameters = new ArrayList<>();
        int size = tags.size();
        for (int i = 1; i <= size; i++) {
            String parameter = ":tagName" + i;
            parameters.add(parameter);
        }
        String values = String.join(VALUES_DELIMITER, parameters);
        return String.format(TEMPLATE_SQL_REMOVE_UNNECESSARY_TAGS, values);
    }

    @Override
    public Map<String, Object> getParameters() {
        Map<String, Object> namedParameters = new HashMap<>();
        Set<Tag> tags = certificate.getTags();
        int count = 0;
        for (Tag tag : tags) {
            ++count;
            String tagName = tag.getName();
            String key = "tagName" + count;
            namedParameters.put(key, tagName);
        }
        Long certificateId = certificate.getId();
        namedParameters.put("certificateId", certificateId);
        return Collections.unmodifiableMap(namedParameters);
    }

}
