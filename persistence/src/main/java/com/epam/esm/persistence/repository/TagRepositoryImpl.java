package com.epam.esm.persistence.repository;

import com.epam.esm.persistence.entity.Tag;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
@Transactional
public class TagRepositoryImpl extends AbstractRepository<Tag> implements TagRepository {

    private static final String SQL_FIND_MOST_PURCHASED_BY_TOP_ACCOUNT = "\n" +
            "SELECT tag.id, \n" +
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
            "LIMIT 1 \n";

    @Override
    public Optional<Tag> findMostPurchasedByTopAccount() {
        return this.findSingleByNativeQuery(SQL_FIND_MOST_PURCHASED_BY_TOP_ACCOUNT);
    }

}
