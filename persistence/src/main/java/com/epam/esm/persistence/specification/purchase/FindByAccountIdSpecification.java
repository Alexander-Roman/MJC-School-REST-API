package com.epam.esm.persistence.specification.purchase;

import com.epam.esm.persistence.entity.Account;
import com.epam.esm.persistence.entity.Account_;
import com.epam.esm.persistence.entity.Purchase;
import com.epam.esm.persistence.entity.Purchase_;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class FindByAccountIdSpecification implements Specification<Purchase> {

    private final Long id;

    public FindByAccountIdSpecification(Long id) {
        this.id = id;
    }

    @Override
    public Predicate toPredicate(Root<Purchase> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        Join<Purchase, Account> accountJoin = root.join(Purchase_.account, JoinType.LEFT);
        Path<Long> accountIdPath = accountJoin.get(Account_.id);
        return criteriaBuilder.equal(accountIdPath, id);
    }

}
