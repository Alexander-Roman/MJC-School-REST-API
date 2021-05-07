package com.epam.esm.web.assember;

import com.epam.esm.persistence.entity.Purchase;
import com.epam.esm.web.controller.AccountController;
import com.epam.esm.web.controller.CertificateController;
import com.epam.esm.web.controller.PurchaseController;
import com.epam.esm.web.mapper.PurchaseMapper;
import com.epam.esm.web.model.AccountDto;
import com.epam.esm.web.model.CertificateDto;
import com.epam.esm.web.model.PurchaseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PurchaseDtoAssemblerImpl extends RepresentationModelAssemblerSupport<Purchase, PurchaseDto> implements PurchaseDtoAssembler {

    private final PurchaseMapper purchaseMapper;

    @Autowired
    public PurchaseDtoAssemblerImpl(PurchaseMapper purchaseMapper) {
        super(PurchaseController.class, PurchaseDto.class);
        this.purchaseMapper = purchaseMapper;
    }

    @Override
    public PurchaseDto toModel(Purchase purchase) {
        PurchaseDto purchaseDto = purchaseMapper.map(purchase);

        Long id = purchaseDto.getId();
        purchaseDto.add(linkTo(methodOn(PurchaseController.class).getPurchaseById(id)).withSelfRel());

        AccountDto accountDto = purchaseDto.getAccount();
        Long accountDtoId = accountDto.getId();
        accountDto.add(linkTo(methodOn(AccountController.class).getAccountById(accountDtoId)).withSelfRel());

        List<CertificateDto> items = purchaseDto.getItems();
        for (CertificateDto certificateDto : items) {
            Long certificateDtoId = certificateDto.getId();
            certificateDto.add(linkTo(methodOn(CertificateController.class).getCertificateById(certificateDtoId)).withSelfRel());
        }

        return purchaseDto;
    }

}
