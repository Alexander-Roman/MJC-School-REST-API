package com.epam.esm.web.mapper;

import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.persistence.entity.Purchase;
import com.epam.esm.web.model.CertificateDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Mapper(componentModel = "spring", uses = {PurchaseItemMapper.class, AccountMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class AbstractPurchaseMapper implements PurchaseMapper {

    private static final Integer DEFAULT_QUANTITY = 1;

    public Set<Purchase.Item> map(List<CertificateDto> purchaseDtoItems) {
        if (purchaseDtoItems == null) {
            return null;
        }

        Map<Long, Integer> quantityMap = new HashMap<>();
        for (CertificateDto certificateDto : purchaseDtoItems) {
            Long id = certificateDto.getId();
            Integer quantity = certificateDto.getQuantity();
            if (quantity == null) {
                quantity = DEFAULT_QUANTITY;
            }
            Integer value = quantityMap.get(id);
            if (value == null) {
                quantityMap.put(id, quantity);
            } else {
                Integer sum = value + quantity;
                quantityMap.put(id, sum);
            }
        }

        Set<Purchase.Item> stackedItems = new HashSet<>();
        for (Map.Entry<Long, Integer> entry : quantityMap.entrySet()) {
            Long id = entry.getKey();
            Integer quantity = entry.getValue();
            Certificate certificate = Certificate
                    .builder()
                    .setId(id)
                    .build();
            Purchase.Item item = new Purchase.Item(null, certificate, quantity);
            stackedItems.add(item);
        }

        return stackedItems;
    }

}
