package com.epam.esm.persistence.specification;

import java.util.Map;

public interface Specification<T> {

    String getQuery();

    Map<String, Object> getParameters();

}
