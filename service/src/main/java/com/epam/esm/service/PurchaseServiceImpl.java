package com.epam.esm.service;

import com.epam.esm.persistence.entity.Account;
import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.persistence.entity.Purchase;
import com.epam.esm.persistence.repository.PurchaseRepository;
import com.epam.esm.service.exception.EntityNotFoundException;
import com.epam.esm.service.model.PurchaseRequest;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class PurchaseServiceImpl implements PurchaseService {

    private static final String ERROR_MESSAGE_ID_INVALID = "Invalid ID parameter: ";
    private static final String ERROR_MESSAGE_PURCHASE_NOT_FOUND = "Purchase does not exists! ID: ";
    private static final long MIN_ID_VALUE = 1L;

    private final PurchaseRepository purchaseRepository;
    private final AccountService accountService;
    private final CertificateService certificateService;

    @Autowired
    public PurchaseServiceImpl(PurchaseRepository purchaseRepository,
                               AccountService accountService,
                               CertificateService certificateService) {
        this.purchaseRepository = purchaseRepository;
        this.accountService = accountService;
        this.certificateService = certificateService;
    }

    @Override
    public Purchase findById(Long id) {
        Preconditions.checkNotNull(id, ERROR_MESSAGE_ID_INVALID + id);
        Preconditions.checkArgument(id >= MIN_ID_VALUE, ERROR_MESSAGE_ID_INVALID + id);

        Optional<Purchase> purchase = purchaseRepository.findById(id);
        return purchase
                .orElseThrow(() -> new EntityNotFoundException(ERROR_MESSAGE_PURCHASE_NOT_FOUND + id));
    }

    @Override
    public Page<Purchase> findPage(Pageable pageable, Specification<Purchase> specification) {
        Preconditions.checkNotNull(pageable, "Pageable argument invalid: " + pageable);
        Preconditions.checkNotNull(specification, "Specification argument invalid: " + specification);

        return purchaseRepository.find(pageable, specification);
    }

    @Override
    @Transactional
    public Purchase createPurchase(PurchaseRequest purchaseRequest) {
        Preconditions.checkNotNull(purchaseRequest, "PurchaseRequest argument invalid: " + purchaseRequest);

        Long accountId = purchaseRequest.getAccountId();
        Account account = accountService.findById(accountId);

        Map<Long, Integer> certificateIdQuantity = purchaseRequest.getCertificateIdQuantity();
        Map<Certificate, Integer> certificateQuantity = new HashMap<>();
        for (Map.Entry<Long, Integer> item : certificateIdQuantity.entrySet()) {
            Long id = item.getKey();
            Certificate certificate = certificateService.findById(id);
            Integer quantity = item.getValue();
            certificateQuantity.put(certificate, quantity);
        }

        BigDecimal cost = certificateQuantity.entrySet()
                .stream()
                .map(entry -> new BigDecimal(entry.getValue()).multiply(entry.getKey().getPrice()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        LocalDateTime date = LocalDateTime.now();
        Purchase purchase = new Purchase(null, account, certificateQuantity, cost, date);
        return purchaseRepository.save(purchase);
    }

}
