package com.epam.esm.web.controller;

import com.epam.esm.persistence.entity.Purchase;
import com.epam.esm.persistence.entity.Purchase_;
import com.epam.esm.persistence.specification.FindAllSpecification;
import com.epam.esm.persistence.specification.purchase.FindByAccountIdSpecification;
import com.epam.esm.service.PurchaseService;
import com.epam.esm.service.model.PurchaseRequest;
import com.epam.esm.web.assember.PurchaseDtoAssembler;
import com.epam.esm.web.mapper.PurchaseMapper;
import com.epam.esm.web.model.PurchaseDto;
import com.epam.esm.web.validator.constraint.AllowedOrderProperties;
import com.epam.esm.web.validator.group.PurchaseCreate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.groups.Default;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/purchases")
@Validated
public class PurchaseController {

    private static final String MSG_CODE_ID_INVALID = "id.invalid";
    private static final String MSG_SORT_INVALID = "sort.invalid";
    private static final long MIN_ID = 1L;
    private static final String REL_ALL_PURCHASES = "purchases";

    private final PurchaseService purchaseService;
    private final PurchaseMapper purchaseMapper;
    private final PagedResourcesAssembler<Purchase> purchasePagedResourcesAssembler;
    private final PurchaseDtoAssembler purchaseDtoAssembler;

    @Autowired
    public PurchaseController(PurchaseService purchaseService,
                              PurchaseMapper purchaseMapper,
                              PagedResourcesAssembler<Purchase> purchasePagedResourcesAssembler,
                              PurchaseDtoAssembler purchaseDtoAssembler) {
        this.purchaseService = purchaseService;
        this.purchaseMapper = purchaseMapper;
        this.purchasePagedResourcesAssembler = purchasePagedResourcesAssembler;
        this.purchaseDtoAssembler = purchaseDtoAssembler;
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseDto> getPurchaseById(
            @PathVariable("id")
            @Min(value = MIN_ID, message = MSG_CODE_ID_INVALID) Long id) {
        Purchase purchase = purchaseService.findById(id);
        PurchaseDto purchaseDto = purchaseDtoAssembler.toModel(purchase);

        purchaseDto.add(linkTo(methodOn(PurchaseController.class).getPurchasePage(null, null)).withRel(REL_ALL_PURCHASES));
        return new ResponseEntity<>(purchaseDto, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<PagedModel<PurchaseDto>> getPurchasePage(
            @RequestParam(value = "account", required = false)
            @Min(value = MIN_ID, message = MSG_CODE_ID_INVALID) Long accountId,
            @AllowedOrderProperties(message = MSG_SORT_INVALID, value = {Purchase_.COST, Purchase_.DATE}) Pageable pageable) {

        Specification<Purchase> specification = new FindAllSpecification<>();
        if (accountId != null) {
            FindByAccountIdSpecification findByAccountIdSpecification = new FindByAccountIdSpecification(accountId);
            specification = specification.and(findByAccountIdSpecification);
        }
        Page<Purchase> purchasePage = purchaseService.findPage(pageable, specification);
        PagedModel<PurchaseDto> purchaseDtoPagedModel = purchasePagedResourcesAssembler.toModel(purchasePage, purchaseDtoAssembler);
        return new ResponseEntity<>(purchaseDtoPagedModel, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<PurchaseDto> createPurchase(@RequestBody @Validated({Default.class, PurchaseCreate.class}) PurchaseDto purchaseDto) {
        PurchaseRequest purchaseRequest = purchaseMapper.map(purchaseDto);
        Purchase created = purchaseService.createPurchase(purchaseRequest);
        PurchaseDto createdDto = purchaseDtoAssembler.toModel(created);

        createdDto.add(linkTo(methodOn(PurchaseController.class).getPurchasePage(null, null)).withRel(REL_ALL_PURCHASES));
        return new ResponseEntity<>(createdDto, HttpStatus.OK);
    }

}
