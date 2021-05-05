package com.epam.esm.persistence.repository;

import com.epam.esm.persistence.entity.Tag;

import java.util.Optional;

/**
 * Repository interface for Tag entity
 */
public interface TagRepository extends Repository<Tag> {

    /**
     * Finds the most widely used tag of a Account with the highest cost of all Purchases
     *
     * @return Optional of Tag found, or empty if found nothing
     */
    Optional<Tag> findMostPurchasedByTopAccount();

}
