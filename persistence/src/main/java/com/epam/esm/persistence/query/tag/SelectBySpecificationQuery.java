package com.epam.esm.persistence.query.tag;

import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.persistence.query.SelectQuery;
import com.epam.esm.persistence.specification.Specification;

import java.util.Map;

public class SelectBySpecificationQuery implements SelectQuery<Tag> {

    Specification<Tag> specification;

    public SelectBySpecificationQuery(Specification<Tag> specification) {
        this.specification = specification;
    }

    @Override
    public String getSql() {
        return specification.getSubQuery();
    }

    @Override
    public Map<String, Object> getParameters() {
        return specification.getParameters();
    }

}
