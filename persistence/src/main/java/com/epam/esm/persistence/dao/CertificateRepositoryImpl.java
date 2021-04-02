package com.epam.esm.persistence.dao;

import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.persistence.model.SortRequest;
import com.epam.esm.persistence.query.SelectQuery;
import com.epam.esm.persistence.query.certificate.SimpleSpecificationQuery;
import com.epam.esm.persistence.query.certificate.SortQuery;
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
        SelectQuery<Certificate> query = new SimpleSpecificationQuery(specification);
        return this.executeSelect(query);
    }

    @Override
    public List<Certificate> findSorted(SortRequest sortRequest, Specification<Certificate> specification) {
        SelectQuery<Certificate> query = new SortQuery(sortRequest, specification);
        return this.executeSelect(query);
    }

}
