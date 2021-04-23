package com.epam.esm.persistence.specification.certificare;

import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.persistence.entity.Certificate_;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class FindNotDeletedByIdSpecification implements Specification<Certificate> {

    private final Long id;

    public FindNotDeletedByIdSpecification(Long id) {
        this.id = id;
    }

    @Override
    public Predicate toPredicate(Root<Certificate> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        Path<Long> idPath = root.get(Certificate_.id);
        Predicate idPredicate = criteriaBuilder.equal(idPath, id);
        Path<Boolean> deletedPath = root.get(Certificate_.deleted);
        Predicate deletedPredicate = criteriaBuilder.equal(deletedPath, false);
        return criteriaBuilder.and(idPredicate, deletedPredicate);
    }
}
