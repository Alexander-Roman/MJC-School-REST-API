package com.epam.esm.persistence.mapper;

import com.epam.esm.persistence.entity.Tag;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class TagRowMapper implements RowMapper<Tag> {

    private static final String COLUMN_TAG_ID = "tag_id";
    private static final String COLUMN_TAG_NAME = "tag_name";

    @Override
    public Tag mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
        Long id = resultSet.getObject(COLUMN_TAG_ID, Long.class);
        String name = resultSet.getString(COLUMN_TAG_NAME);
        return new Tag(id, name, null);
    }

}
