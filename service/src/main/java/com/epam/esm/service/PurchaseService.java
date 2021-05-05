package com.epam.esm.service;

import com.epam.esm.persistence.entity.Purchase;
import com.epam.esm.service.exception.EntityNotFoundException;
import com.epam.esm.service.model.PurchaseRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

/**
 * Business logic interface for Purchases
 */
public interface PurchaseService {

    /**
     * Finds Purchase by specified ID
     *
     * @param id of Purchase to find
     * @return Purchase found
     * @throws NullPointerException     when id is null
     * @throws IllegalArgumentException when id is not valid
     * @throws EntityNotFoundException  when found noting
     */
    Purchase findById(Long id);

    /**
     * Finds page of Purchases based on specification and pagination parameters.
     *
     * @param pageable      object with pagination parameters
     * @param specification of Purchase type
     * @return Page of Purchases found
     * @throws NullPointerException when any of arguments is null
     */
    Page<Purchase> findPage(Pageable pageable, Specification<Purchase> specification);

    /**
     * Creates new Purchase based on PurchaseRequest object, which contains Account ID,
     * IDs of Certificates being purchased and quantity each
     *
     * @param purchaseRequest core information of purchase
     * @return Purchase created
     */
    Purchase createPurchase(PurchaseRequest purchaseRequest);

}
