package com.epam.esm.persistence.query.tag;

import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.persistence.query.UpdateQuery;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TagDeleteByIdQuery implements UpdateQuery<Tag> {

    private static final String SQL_DELETE_BY_ID = "\n" +
            "DELETE FROM tag \n" +
            "WHERE id = :id \n";

    private final Long id;

    public TagDeleteByIdQuery(Long id) {
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
