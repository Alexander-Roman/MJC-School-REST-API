package com.epam.esm.persistence.dao;

import com.epam.esm.persistence.query.SelectQuery;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;
import java.util.Map;

public class AbstractRepository<T> {

    private static final Logger LOGGER = LogManager.getLogger(AbstractRepository.class);

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final ResultSetExtractor<List<T>> resultSetExtractor;

    public AbstractRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                              ResultSetExtractor<List<T>> resultSetExtractor) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.resultSetExtractor = resultSetExtractor;
    }

    protected List<T> executeSelect(SelectQuery<T> query) {
        String sql = query.getSql();
        Map<String, Object> namedParameters = query.getParameters();

        LOGGER.debug(sql);
        LOGGER.debug("Parameters:" + namedParameters);

        return namedParameterJdbcTemplate.query(sql, namedParameters, resultSetExtractor);
    }

}
