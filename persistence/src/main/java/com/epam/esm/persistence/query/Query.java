package com.epam.esm.persistence.query;

import java.util.Map;

public interface Query<T> {

    String getSql();

    Map<String, Object> getParameters();

}
