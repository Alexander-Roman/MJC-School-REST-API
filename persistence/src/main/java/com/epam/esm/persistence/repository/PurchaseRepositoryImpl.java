package com.epam.esm.persistence.repository;

import com.epam.esm.persistence.entity.Purchase;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class PurchaseRepositoryImpl extends AbstractRepository<Purchase> implements PurchaseRepository {

}
