package com.epam.esm.persistence.specification.certificate;

import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.persistence.entity.Certificate_;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class NameOrDescriptionSearchSpecification implements Specification<Certificate> {

    private final String search;

    public NameOrDescriptionSearchSpecification(String search) {
        this.search = search;
    }

    @Override
    public Predicate toPredicate(Root<Certificate> certificateRoot, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        String searchInLowerCase = search.toLowerCase();

        Path<String> namePath = certificateRoot.get(Certificate_.name);
        Expression<String> nameToLowerCase = criteriaBuilder.lower(namePath);
        Predicate nameMatches = criteriaBuilder.like(nameToLowerCase, "%" + searchInLowerCase + "%");

        Path<String> descriptionPath = certificateRoot.get(Certificate_.description);
        Expression<String> descriptionToLowerCase = criteriaBuilder.lower(descriptionPath);
        Predicate descriptionMatches = criteriaBuilder.like(descriptionToLowerCase, "%" + searchInLowerCase + "%");

        return criteriaBuilder.or(nameMatches, descriptionMatches);
    }

}
