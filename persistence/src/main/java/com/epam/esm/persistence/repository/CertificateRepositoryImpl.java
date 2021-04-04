package com.epam.esm.persistence.repository;

import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.persistence.exception.PersistenceException;
import com.epam.esm.persistence.model.SortRequest;
import com.epam.esm.persistence.query.SelectQuery;
import com.epam.esm.persistence.query.UpdateQuery;
import com.epam.esm.persistence.query.certificate.*;
import com.epam.esm.persistence.specification.Specification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CertificateRepositoryImpl extends AbstractRepository<Certificate> implements CertificateRepository {

    @Autowired
    public CertificateRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                                     ResultSetExtractor<List<Certificate>> resultSetExtractor) {
        super(namedParameterJdbcTemplate, resultSetExtractor);
    }

    @Override
    public List<Certificate> find(Specification<Certificate> specification) {
        SelectQuery<Certificate> query = new SelectBySpecificationQuery(specification);
        return this.executeSelect(query);
    }

    @Override
    public List<Certificate> find(SortRequest sortRequest, Specification<Certificate> specification) {
        SelectQuery<Certificate> query = new SelectSortedBySpecificationQuery(sortRequest, specification);
        return this.executeSelect(query);
    }

    @Override
    public Certificate create(Certificate certificate) {
        UpdateQuery<Certificate> query = new CertificateCreateQuery(certificate);
        List<Long> keys = this.executeUpdate(query);
        Long id = keys
                .stream()
                .findFirst()
                .orElseThrow(() -> new PersistenceException("Unexpected query result!"));
        return Certificate.Builder
                .from(certificate)
                .setId(id)
                .build();
    }

    @Override
    public Certificate update(Certificate certificate) {
        UpdateQuery<Certificate> query = new CertificateUpdateQuery(certificate);
        this.executeUpdate(query);
        return certificate;
    }

    @Override
    public void delete(Long id) {
        UpdateQuery<Certificate> query = new CertificateDeleteByIdQuery(id);
        this.executeUpdate(query);
    }

}
