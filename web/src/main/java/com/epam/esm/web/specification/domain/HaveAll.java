package com.epam.esm.web.specification.domain;

import net.kaczmarzyk.spring.data.jpa.domain.PathSpecification;
import net.kaczmarzyk.spring.data.jpa.utils.Converter;
import net.kaczmarzyk.spring.data.jpa.utils.QueryContext;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HaveAll<T> extends PathSpecification<T> {

    private static final long serialVersionUID = 1L;

    private final String[] allowedValues;
    private final Converter converter;

    public HaveAll(QueryContext queryContext, String path, String[] httpParamValues, Converter converter) {
        super(queryContext, path);
        if (httpParamValues == null || httpParamValues.length < 1) {
            throw new IllegalArgumentException();
        }
        this.allowedValues = httpParamValues;
        this.converter = converter;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        Path<?> path = path(root);
        Class<?> typeOnPath = path.getJavaType();

        List<Predicate> predicates = new ArrayList<>();
        for (String value : allowedValues) {
            Subquery<T> subQuery = this.buildSubQuery(root, query, criteriaBuilder, value, typeOnPath);
            Predicate predicate = criteriaBuilder.in(root).value(subQuery);
            predicates.add(predicate);
        }

        int size = predicates.size();
        Predicate[] restrictions = new Predicate[size];
        predicates.toArray(restrictions);

        return criteriaBuilder.and(restrictions);
    }


    @SuppressWarnings("unchecked")
    private Subquery<T> buildSubQuery(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder, String value, Class<?> typeOnPath) {
        Class<T> rootType = (Class<T>) root.getJavaType();
        Subquery<T> subQuery = query.subquery(rootType);
        Root<T> subQueryRoot = subQuery.from(rootType);
        Path<String> subQueryPath = this.path(subQueryRoot);
        String converted = (String) converter.convert(value, typeOnPath);
        Predicate predicate = criteriaBuilder.equal(criteriaBuilder.lower(subQueryPath), converted.toLowerCase());
        return subQuery.select(subQueryRoot).where(predicate);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Arrays.hashCode(allowedValues);
        result = prime * result + ((converter == null) ? 0 : converter.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        HaveAll other = (HaveAll) obj;
        if (!Arrays.equals(allowedValues, other.allowedValues))
            return false;
        if (converter == null) {
            return other.converter == null;
        } else {
            return converter.equals(other.converter);
        }
    }

    @Override
    public String toString() {
        return "HaveAll [allowedValues=" + Arrays.toString(allowedValues) + ", converter=" + converter + "]";
    }
}
