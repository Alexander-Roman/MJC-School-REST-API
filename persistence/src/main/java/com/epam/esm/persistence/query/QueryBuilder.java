package com.epam.esm.persistence.query;

import com.epam.esm.persistence.model.SortRequest;
import com.epam.esm.persistence.specification.Specification;

public interface QueryBuilder<T> {

    String getSortQuery(Specification<T> specification, SortRequest sortRequest);
}
