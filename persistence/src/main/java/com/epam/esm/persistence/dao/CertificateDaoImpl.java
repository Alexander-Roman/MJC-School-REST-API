package com.epam.esm.persistence.dao;

import com.epam.esm.persistence.clause.SqlCertificateOrderClauseBuilder;
import com.epam.esm.persistence.clause.SqlCertificateWhereClauseBuilder;
import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.persistence.model.FilterRequest;
import com.epam.esm.persistence.model.SortRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class CertificateDaoImpl implements CertificateDao {

    private static final String SQL_FIND_BY_ID = "\n" +
            "SELECT certificate.id AS certificate_id, \n" +
            "       certificate.name AS certificate_name, \n" +
            "       certificate.description, \n" +
            "       certificate.price, \n" +
            "       certificate.duration, \n" +
            "       certificate.create_date, \n" +
            "       certificate.last_update_date, \n" +
            "       tag.id AS tag_id, \n" +
            "       tag.name AS tag_name \n" +
            "FROM certificate \n" +
            "         LEFT JOIN certificate_tag ON certificate.id = certificate_tag.certificate_id \n" +
            "         LEFT JOIN tag ON certificate_tag.tag_id = tag.id \n" +
            "WHERE certificate.id = :certificateId \n";

    private static final String SQL_FIND_ALL = "\n" +
            "SELECT certificate.id AS certificate_id, \n" +
            "       certificate.name AS certificate_name, \n" +
            "       certificate.description, \n" +
            "       certificate.price, \n" +
            "       certificate.duration, \n" +
            "       certificate.create_date, \n" +
            "       certificate.last_update_date, \n" +
            "       tag.id AS tag_id, \n" +
            "       tag.name AS tag_name \n" +
            "FROM certificate \n" +
            "         LEFT JOIN certificate_tag ON certificate.id = certificate_tag.certificate_id \n" +
            "         LEFT JOIN tag ON certificate_tag.tag_id = tag.id \n";

    private static final String SQL_INSERT = "\n" +
            "INSERT INTO certificate (name, description, price, duration, create_date, last_update_date) \n" +
            "VALUES (:name, :description, :price, :duration, :createDate, :lastUpdateDate) \n";

    private static final String SQL_UPDATE = "\n" +
            "UPDATE certificate \n" +
            "SET name             = :name, \n" +
            "    description      = :description, \n" +
            "    price            = :price, \n" +
            "    duration         = :duration, \n" +
            "    create_date      = :createDate, \n" +
            "    last_update_date = :lastUpdateDate \n" +
            "WHERE id = :id  \n";

    private static final String SQL_DELETE_BY_ID = "\n" +
            "DELETE FROM certificate \n" +
            "WHERE id = :id \n";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final ResultSetExtractor<List<Certificate>> certificateListExtractor;
    private final ResultSetExtractor<Optional<Certificate>> certificateExtractor;

    @Autowired
    public CertificateDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                              ResultSetExtractor<List<Certificate>> certificateListExtractor,
                              ResultSetExtractor<Optional<Certificate>> certificateExtractor) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.certificateListExtractor = certificateListExtractor;
        this.certificateExtractor = certificateExtractor;
    }

    @Override
    public Optional<Certificate> findById(Long certificateId) {
        Map<String, Object> namedParameters = new HashMap<>();
        namedParameters.put("certificateId", certificateId);
        return namedParameterJdbcTemplate.query(SQL_FIND_BY_ID, namedParameters, certificateExtractor);
    }

    @Override
    public List<Certificate> findAll() {
        return namedParameterJdbcTemplate.query(SQL_FIND_ALL, certificateListExtractor);
    }

    @Override
    public List<Certificate> findAll(SortRequest sortRequest, FilterRequest filterRequest) {
        String whereClause = SqlCertificateWhereClauseBuilder.buildSqlWhereClause(filterRequest);
        String orderByeClause = SqlCertificateOrderClauseBuilder.buildSqlOrderClause(sortRequest);
        String sql = SQL_FIND_ALL
                .concat(whereClause)
                .concat(orderByeClause);
        SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(filterRequest);
        return namedParameterJdbcTemplate.query(sql, sqlParameterSource, certificateListExtractor);
    }

    @Override
    public Certificate create(Certificate certificate) {
        SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(certificate);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(SQL_INSERT, sqlParameterSource, keyHolder, new String[]{"id"});
        Long id = keyHolder.getKeyAs(Long.class);
        return Certificate.Builder
                .from(certificate)
                .setId(id)
                .build();
    }

    @Override
    public Certificate update(Certificate certificate) {
        SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(certificate);
        namedParameterJdbcTemplate.update(SQL_UPDATE, sqlParameterSource);
        return certificate;
    }

    @Override
    public void delete(Long id) {
        Map<String, Object> namedParameters = new HashMap<>();
        namedParameters.put("id", id);
        namedParameterJdbcTemplate.update(SQL_DELETE_BY_ID, namedParameters);
    }

}
