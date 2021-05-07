package com.epam.esm.persistence.specification.account;

import com.epam.esm.persistence.entity.Account;
import com.epam.esm.persistence.entity.Account_;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class FindByEmailSpecification implements Specification<Account> {

    private final String email;

    public FindByEmailSpecification(String email) {
        this.email = email;
    }

    @Override
    public Predicate toPredicate(Root<Account> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        Path<String> emailPath = root.get(Account_.email);
        Expression<String> emailPathToLowerCase = criteriaBuilder.lower(emailPath);
        String emailInLowerCase = email.toLowerCase();
        return criteriaBuilder.equal(emailPathToLowerCase, emailInLowerCase);
    }

}
