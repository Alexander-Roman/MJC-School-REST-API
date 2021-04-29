package com.epam.esm.web.mapper;

import com.epam.esm.persistence.entity.Purchase;
import com.epam.esm.web.model.PurchaseDto;

public interface PurchaseMapper {

    PurchaseDto map(Purchase purchase);

    Purchase map(PurchaseDto purchaseDto);

}
