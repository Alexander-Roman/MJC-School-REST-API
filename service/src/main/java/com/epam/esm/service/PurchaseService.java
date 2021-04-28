package com.epam.esm.service;

import com.epam.esm.persistence.entity.Purchase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface PurchaseService {

    Purchase findById(Long id);

    Page<Purchase> findPage(Pageable pageable, Specification<Purchase> specification);

    Purchase createPurchase(Purchase purchase);

}
