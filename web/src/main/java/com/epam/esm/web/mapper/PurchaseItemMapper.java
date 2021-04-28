package com.epam.esm.web.mapper;

import com.epam.esm.persistence.entity.Purchase;
import com.epam.esm.web.model.CertificateDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = TagMapper.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface PurchaseItemMapper {

    @Mapping(source = "count", target = "quantity")
    @Mapping(source = "certificate", target = ".")
    CertificateDto map(Purchase.Item item);

}
