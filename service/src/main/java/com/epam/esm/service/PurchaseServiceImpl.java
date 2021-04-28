package com.epam.esm.service;

import com.epam.esm.persistence.entity.Account;
import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.persistence.entity.Purchase;
import com.epam.esm.persistence.repository.PurchaseRepository;
import com.epam.esm.service.exception.EntityNotFoundException;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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
    public Purchase createPurchase(Purchase purchase) {
        Preconditions.checkNotNull(purchase, "Purchase argument invalid: " + purchase);

        Account account = purchase.getAccount();
        Long accountId = account.getId();
        Account accountFound = accountService.findById(accountId);

        Set<Purchase.Item> items = purchase.getItems();
        Set<Purchase.Item> verifiedItems = new HashSet<>();
        for (Purchase.Item item : items) {
            Certificate certificate = item.getCertificate();
            Long id = certificate.getId();
            Certificate found = certificateService.findById(id);
            Purchase.Item verified = Purchase.Item.Builder
                    .from(item)
                    .setCertificate(found)
                    .build();
            verifiedItems.add(verified);
        }

        BigDecimal cost = BigDecimal.ZERO;
        for (Purchase.Item item : verifiedItems) {
            Certificate certificate = item.getCertificate();
            BigDecimal price = certificate.getPrice();
            Integer count = item.getCount();
            BigDecimal subtotal = new BigDecimal(count).multiply(price);
            cost = cost.add(subtotal);
        }

        LocalDateTime date = LocalDateTime.now();
        Purchase assembled = new Purchase(null, accountFound, verifiedItems, cost, date);
        return purchaseRepository.save(assembled);
    }

}
