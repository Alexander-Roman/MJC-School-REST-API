package com.epam.esm.persistence.specification.tag;

import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.persistence.entity.Tag_;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class FindByNameSpecification implements Specification<Tag> {

    private final String name;

    public FindByNameSpecification(String name) {
        this.name = name;
    }

    @Override
    public Predicate toPredicate(Root<Tag> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        Path<String> namePath = root.get(Tag_.name);
        return criteriaBuilder.equal(namePath, name);
    }

}
