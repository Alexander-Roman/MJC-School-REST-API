package com.epam.esm.persistence.repository;

import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.persistence.exception.PersistenceException;
import com.epam.esm.persistence.query.SelectQuery;
import com.epam.esm.persistence.query.UpdateQuery;
import com.epam.esm.persistence.query.tag.*;
import com.epam.esm.persistence.specification.Specification;
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
    public List<Tag> find(Specification<Tag> specification) {
        SelectQuery<Tag> query = new SelectBySpecificationQuery(specification);
        return this.executeSelect(query);
    }

    @Override
    public Tag create(Tag tag) {
        UpdateQuery<Tag> query = new TagCreateQuery(tag);
        List<Long> keys = this.executeUpdate(query);
        Long id = keys
                .stream()
                .findFirst()
                .orElseThrow(() -> new PersistenceException("Unexpected query result!"));
        return Tag.Builder
                .from(tag)
                .setId(id)
                .build();
    }

    @Override
    public void delete(Long id) {
        UpdateQuery<Tag> query = new TagDeleteByIdQuery(id);
        this.executeUpdate(query);
    }

    @Override
    public List<Long> createIfNotExists(Set<Tag> tags) {
        UpdateQuery<Tag> query = new PgCreateIfNotExistsQuery(tags);
        return this.executeUpdate(query);
    }

    @Override
    public void deleteUnused() {
        UpdateQuery<Tag> query = new DeleteUnusedQuery();
        this.executeUpdate(query);
    }

}
