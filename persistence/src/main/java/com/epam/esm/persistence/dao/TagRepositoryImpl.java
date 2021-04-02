package com.epam.esm.persistence.dao;

import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.persistence.query.UpdateQuery;
import com.epam.esm.persistence.query.tag.SaveIfNotExistQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public class TagRepositoryImpl extends AbstractRepository<Tag> implements TagRepository {

    @Autowired
    public TagRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                             ResultSetExtractor<List<Tag>> resultSetExtractor) {
        super(namedParameterJdbcTemplate, resultSetExtractor);
    }

    @Override
    public List<Long> saveIfNotExist(Set<Tag> tags) {
        UpdateQuery<Tag> query = new SaveIfNotExistQuery(tags);
        return this.executeUpdate(query);
    }

}
