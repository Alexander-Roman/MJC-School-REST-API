package com.epam.esm.persistence.query.tag;

import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.persistence.query.UpdateQuery;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TagCreateQuery implements UpdateQuery<Tag> {

    private static final String SQL_INSERT = "INSERT INTO tag (name) VALUES (:name) \n";

    private final Tag tag;

    public TagCreateQuery(Tag tag) {
        this.tag = tag;
    }

    @Override
    public String getSql() {
        return SQL_INSERT;
    }

    @Override
    public Map<String, Object> getParameters() {
        Map<String, Object> namedParameters = new HashMap<>();
        String name = tag.getName();
        namedParameters.put("name", name);
        return Collections.unmodifiableMap(namedParameters);
    }

}
