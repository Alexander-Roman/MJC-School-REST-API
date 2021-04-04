package com.epam.esm.persistence.query.certificatetag;

import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.persistence.entity.CertificateTag;
import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.persistence.query.UpdateQuery;

import java.util.*;
import java.util.stream.Collectors;

public class PgResolveAddedTagsByNamesQuery implements UpdateQuery<CertificateTag> {

    private static final String TEMPLATE_SQL_ADD_TAGS_BY_NAMES = "\n" +
            "INSERT INTO certificate_tag (certificate_id, tag_id) \n" +
            "VALUES %s ON CONFLICT (certificate_id, tag_id) DO NOTHING \n";
    private static final String TEMPLATE_VALUE_SET = "(:certificateId, (SELECT tag.id FROM tag WHERE tag.name = %s))";
    private static final String VALUE_SET_DELIMITER = ", ";

    private final Certificate certificate;

    public PgResolveAddedTagsByNamesQuery(Certificate certificate) {
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
        String valueSet = parameters
                .stream()
                .map(tagName -> String.format(TEMPLATE_VALUE_SET, tagName))
                .collect(Collectors.joining(VALUE_SET_DELIMITER));
        return String.format(TEMPLATE_SQL_ADD_TAGS_BY_NAMES, valueSet);
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
