package com.epam.esm.persistence.specification;

import java.util.Map;

public interface Specification<T> {

    String getSubQuery();

    Map<String, Object> getParameters();

}
