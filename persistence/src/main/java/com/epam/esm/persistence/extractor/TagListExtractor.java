package com.epam.esm.persistence.extractor;

import com.epam.esm.persistence.entity.Tag;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class TagListExtractor implements ResultSetExtractor<List<Tag>> {

    private static final String COLUMN_TAG_ID = "tag_id";
    private static final String COLUMN_TAG_NAME = "tag_name";

    @Override
    public List<Tag> extractData(ResultSet resultSet) throws DataAccessException, SQLException {
        List<Tag> tags = new ArrayList<>();
        while (resultSet.next()) {
            Long id = resultSet.getObject(COLUMN_TAG_ID, Long.class);
            String name = resultSet.getString(COLUMN_TAG_NAME);
            Tag tag = new Tag(id, name);
            tags.add(tag);
        }
        return tags;
    }

}
