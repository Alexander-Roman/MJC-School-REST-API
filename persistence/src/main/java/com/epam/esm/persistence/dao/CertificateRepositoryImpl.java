package com.epam.esm.persistence.dao;

import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.persistence.specification.Specification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class CertificateRepositoryImpl implements CertificateRepository {

    private static final Logger LOGGER = LogManager.getLogger(CertificateRepositoryImpl.class);

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final ResultSetExtractor<List<Certificate>> resultSetExtractor;

    @Autowired
    public CertificateRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                                     ResultSetExtractor<List<Certificate>> resultSetExtractor) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.resultSetExtractor = resultSetExtractor;
    }

    @Override
    public List<Certificate> find(Specification<Certificate> specification) {
        String sql = specification.getQuery();
        Map<String, Object> namedParameters = specification.getParameters();

        LOGGER.debug(sql);
        LOGGER.debug("Parameters:" + namedParameters);

        return namedParameterJdbcTemplate.query(sql, namedParameters, resultSetExtractor);
    }
}
