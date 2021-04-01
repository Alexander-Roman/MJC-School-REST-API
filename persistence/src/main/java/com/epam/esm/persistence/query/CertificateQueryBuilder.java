package com.epam.esm.persistence.query;

import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.persistence.model.SortRequest;
import com.epam.esm.persistence.specification.Specification;
import org.springframework.stereotype.Component;

@Component
public class CertificateQueryBuilder implements QueryBuilder<Certificate> {

    private final SqlOrderClauseFactory<Certificate> certificateSqlOrderClauseFactory;

    public CertificateQueryBuilder(SqlOrderClauseFactory<Certificate> certificateSqlOrderClauseFactory) {
        this.certificateSqlOrderClauseFactory = certificateSqlOrderClauseFactory;
    }

    @Override
    public String getSortQuery(Specification<Certificate> specification, SortRequest sortRequest) {
        String orderClause = certificateSqlOrderClauseFactory.getOrderClause(sortRequest);
        String query = specification.getQuery();
        return query.concat(orderClause);
    }
}
