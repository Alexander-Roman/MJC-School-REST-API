package com.epam.esm.persistence.repository;

import com.epam.esm.persistence.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for Tag entity
 */
@Repository
public interface TagRepository extends JpaRepository<Tag, Long>, JpaSpecificationExecutor<Tag> {

    /**
     * Finds the most widely used tag of a Account with the highest cost of all Purchases
     *
     * @return Optional of Tag found, or empty if found nothing
     */
    @Query(
            value = "SELECT tag.id, \n" +
                    "       tag.name \n" +
                    "FROM purchase \n" +
                    "         LEFT JOIN purchase_item ON purchase.id = purchase_item.purchase_id \n" +
                    "         LEFT JOIN certificate ON purchase_item.certificate_id = certificate.id \n" +
                    "         INNER JOIN certificate_tag ON certificate.id = certificate_tag.certificate_id \n" +
                    "         LEFT JOIN tag ON certificate_tag.tag_id = tag.id \n" +
                    "WHERE purchase.account_id = (SELECT purchase.account_id \n" +
                    "                             FROM purchase \n" +
                    "                             GROUP BY purchase.account_id \n" +
                    "                             ORDER BY SUM(purchase.cost) DESC \n" +
                    "                             LIMIT 1) \n" +
                    "GROUP BY tag.id, tag.name, purchase.account_id \n" +
                    "ORDER BY SUM(purchase_item.quantity) DESC \n" +
                    "LIMIT 1 \n",
            nativeQuery = true)
    Optional<Tag> findMostPurchasedByTopAccount();

}
