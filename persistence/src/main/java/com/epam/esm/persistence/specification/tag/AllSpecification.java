package com.epam.esm.persistence.specification.tag;

import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.persistence.specification.Specification;

import java.util.Collections;
import java.util.Map;

public class AllSpecification implements Specification<Tag> {

    private static final String SQL_FIND_ALL = "\n" +
            "SELECT tag.id   AS tag_id, \n" +
            "       tag.name AS tag_name \n" +
            "FROM tag \n";

    @Override
    public String getSubQuery() {
        return SQL_FIND_ALL;
    }

    @Override
    public Map<String, Object> getParameters() {
        return Collections.emptyMap();
    }

}
