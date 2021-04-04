package com.epam.esm.persistence.query.tag;

import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.persistence.query.UpdateQuery;

import java.util.Collections;
import java.util.Map;

public class DeleteUnusedQuery implements UpdateQuery<Tag> {

    private static final String SQL_DELETE_UNUSED = "\n" +
            "DELETE FROM tag \n" +
            "WHERE id NOT IN (SELECT DISTINCT tag_id FROM certificate_tag); \n";

    @Override
    public String getSql() {
        return SQL_DELETE_UNUSED;
    }

    @Override
    public Map<String, Object> getParameters() {
        return Collections.emptyMap();
    }

}
