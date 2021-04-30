package com.epam.esm.web.mapper;

import com.epam.esm.persistence.entity.Purchase;
import com.epam.esm.service.model.PurchaseRequest;
import com.epam.esm.web.model.PurchaseDto;
import org.mapstruct.Mapping;

public interface PurchaseMapper {

    @Mapping(source = "certificateQuantity", target = "items")
    PurchaseDto map(Purchase purchase);

    @Mapping(source = "account.id", target = "accountId")
    @Mapping(source = "items", target = "certificateIdQuantity")
    PurchaseRequest map(PurchaseDto purchaseDto);

}
