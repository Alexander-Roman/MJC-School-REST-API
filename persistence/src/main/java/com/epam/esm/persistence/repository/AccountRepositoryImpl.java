package com.epam.esm.persistence.repository;

import com.epam.esm.persistence.entity.Account;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public class AccountRepositoryImpl extends AbstractRepository<Account> implements AccountRepository {

}
