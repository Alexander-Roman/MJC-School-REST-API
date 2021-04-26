package com.epam.esm.persistence.repository;

import com.epam.esm.persistence.entity.Purchase;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Repository
@Transactional
public class PurchaseRepositoryImpl extends AbstractRepository<Purchase> implements PurchaseRepository {

    private final ItemRepository itemRepository;

    public PurchaseRepositoryImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public Purchase save(Purchase entity) {
        Set<Purchase.Item> items = entity.getItems();
        itemRepository.batchSave(items);
        return super.save(entity);
    }
}
