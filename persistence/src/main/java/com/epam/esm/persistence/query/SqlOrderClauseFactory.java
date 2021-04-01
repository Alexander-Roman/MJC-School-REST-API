package com.epam.esm.persistence.query;

import com.epam.esm.persistence.model.SortRequest;

public interface SqlOrderClauseFactory<T> {

    String getOrderClause(SortRequest sortRequest);

}
