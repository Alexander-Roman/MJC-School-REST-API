package com.epam.esm.persistence.specification.tag;

import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.persistence.entity.Tag_;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class FindByIdSpecification implements Specification<Tag> {

    private final Long id;

    public FindByIdSpecification(Long id) {
        this.id = id;
    }

    @Override
    public Predicate toPredicate(Root<Tag> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        Path<Long> idPath = root.get(Tag_.id);
        return criteriaBuilder.equal(idPath, id);
    }

}
