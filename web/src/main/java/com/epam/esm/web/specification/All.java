package com.epam.esm.web.specification;

import net.kaczmarzyk.spring.data.jpa.domain.PathSpecification;
import net.kaczmarzyk.spring.data.jpa.domain.ZeroArgSpecification;
import net.kaczmarzyk.spring.data.jpa.utils.QueryContext;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class All<T> extends PathSpecification<T> implements ZeroArgSpecification {

    public All(QueryContext queryContext, String path, String... args) {
        super(queryContext, path);
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.conjunction();
    }

}
