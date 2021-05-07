package com.epam.esm.web.mapper;

import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.persistence.entity.Purchase;
import com.epam.esm.service.model.PurchaseRequest;
import com.epam.esm.web.model.CertificateDto;
import com.epam.esm.web.model.PurchaseDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = AccountMapper.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class AbstractPurchaseMapper implements PurchaseMapper {

    private static final Integer DEFAULT_QUANTITY = 1;

    @Override
    @Mapping(source = "certificateQuantity", target = "items")
    public abstract PurchaseDto map(Purchase purchase);

    @Override
    @Mapping(source = "account.id", target = "accountId")
    @Mapping(source = "items", target = "certificateIdQuantity")
    public abstract PurchaseRequest map(PurchaseDto purchaseDto);

    @Mapping(source = "key", target = ".")
    @Mapping(source = "value", target = "quantity")
    @Mapping(target = "tags", ignore = true)
    public abstract CertificateDto map(Map.Entry<Certificate, Integer> certificateQuantity);

    public List<CertificateDto> map(Map<Certificate, Integer> certificateQuantity) {
        return certificateQuantity
                .entrySet()
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }


    public Map<Long, Integer> map(List<CertificateDto> purchaseDtoItems) {
        Map<Long, Integer> certificateIdQuantity = new HashMap<>();
        for (CertificateDto certificateDto : purchaseDtoItems) {
            Long id = certificateDto.getId();
            Integer quantity = certificateDto.getQuantity();
            if (quantity == null) {
                quantity = DEFAULT_QUANTITY;
            }
            Integer value = certificateIdQuantity.get(id);
            if (value == null) {
                certificateIdQuantity.put(id, quantity);
            } else {
                Integer sum = value + quantity;
                certificateIdQuantity.put(id, sum);
            }
        }
        return certificateIdQuantity;
    }

}
