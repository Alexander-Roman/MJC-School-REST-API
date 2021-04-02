package com.epam.esm.persistence.query.certificate;

import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.persistence.query.SelectQuery;
import com.epam.esm.persistence.specification.Specification;

import java.util.Map;

public class SimpleSpecificationQuery implements SelectQuery<Certificate> {

    Specification<Certificate> specification;

    public SimpleSpecificationQuery(Specification<Certificate> specification) {
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
