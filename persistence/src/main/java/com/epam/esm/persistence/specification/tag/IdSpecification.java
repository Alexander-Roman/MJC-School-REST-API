package com.epam.esm.persistence.specification.tag;

import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.persistence.specification.Specification;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class IdSpecification implements Specification<Tag> {

    private static final String SQL_FIND_BY_ID = "\n" +
            "SELECT tag.id   AS tag_id, \n" +
            "       tag.name AS tag_name \n" +
            "FROM tag \n" +
            "WHERE tag.id = :tagId \n";

    private final Long tagId;

    public IdSpecification(Long tagId) {
        this.tagId = tagId;
    }

    @Override
    public String getSubQuery() {
        return SQL_FIND_BY_ID;
    }

    @Override
    public Map<String, Object> getParameters() {
        Map<String, Object> namedParameters = new HashMap<>();
        namedParameters.put("tagId", tagId);
        return Collections.unmodifiableMap(namedParameters);
    }
}
