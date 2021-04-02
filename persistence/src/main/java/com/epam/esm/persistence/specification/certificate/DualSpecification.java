package com.epam.esm.persistence.specification.certificate;

import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.persistence.specification.Specification;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DualSpecification implements Specification<Certificate> {

    private static final String TEMPLATE_SQL_DUAL_SPECIFICATION_QUERY =
            "(%s INTERSECT %s) \n";

    private final Specification<Certificate> firstSpecification;
    private final Specification<Certificate> secondSpecification;

    public DualSpecification(Specification<Certificate> firstSpecification,
                             Specification<Certificate> secondSpecification) {
        this.firstSpecification = firstSpecification;
        this.secondSpecification = secondSpecification;
    }

    @Override
    public String getSubQuery() {
        String firstQuery = firstSpecification.getSubQuery();
        String secondQuery = secondSpecification.getSubQuery();
        return String.format(TEMPLATE_SQL_DUAL_SPECIFICATION_QUERY, firstQuery, secondQuery);
    }

    @Override
    public Map<String, Object> getParameters() {
        Map<String, Object> namedParameters = new HashMap<>();
        Map<String, Object> firstQueryParameters = firstSpecification.getParameters();
        Map<String, Object> secondQueryParameters = secondSpecification.getParameters();
        namedParameters.putAll(firstQueryParameters);
        namedParameters.putAll(secondQueryParameters);
        return Collections.unmodifiableMap(namedParameters);
    }

}
