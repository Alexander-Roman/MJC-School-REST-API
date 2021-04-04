package com.epam.esm.persistence.repository;

import com.epam.esm.persistence.entity.Identifiable;
import com.epam.esm.persistence.query.SelectQuery;
import com.epam.esm.persistence.query.UpdateQuery;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.util.*;

public class AbstractRepository<T extends Identifiable> {

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

    protected List<Long> executeUpdate(UpdateQuery<T> query) {
        String sql = query.getSql();
        Map<String, Object> namedParameters = query.getParameters();

        LOGGER.debug(sql);
        LOGGER.debug("Parameters:" + namedParameters);

        SqlParameterSource parameterSource = new MapSqlParameterSource(namedParameters);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql, parameterSource, keyHolder);

        List<Map<String, Object>> keyList = keyHolder.getKeyList();
        LOGGER.debug("Keys generated: " + keyList);

        List<Long> ids = new ArrayList<>();
        for (Map<String, Object> objectKeyMap : keyList) {
            Optional<Object> value = objectKeyMap
                    .values()
                    .stream()
                    .findFirst();
            if (value.isPresent()) {
                Long id = (Long) value.get();
                ids.add(id);
            }
        }
        return Collections.unmodifiableList(ids);
    }

}
