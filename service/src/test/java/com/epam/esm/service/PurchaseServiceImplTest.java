package com.epam.esm.service;

import com.epam.esm.persistence.entity.Account;
import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.persistence.entity.Purchase;
import com.epam.esm.persistence.entity.Role;
import com.epam.esm.persistence.repository.PurchaseRepository;
import com.epam.esm.persistence.specification.FindAllSpecification;
import com.epam.esm.service.exception.EntityNotFoundException;
import com.epam.esm.service.model.PurchaseRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PurchaseServiceImplTest {

    private static final Long ID_VALID = 1L;
    private static final Long ID_INVALID = -1L;
    private static final Account ACCOUNT = new Account(ID_VALID, "email@mail.com", "password", "name", Role.USER, false);
    private static final LocalDateTime NOW = LocalDateTime.now();
    private static final Certificate CERTIFICATE = new Certificate(ID_VALID, "name", "description", BigDecimal.ONE, 1, NOW, NOW, new HashSet<>(), false);
    private static final Map<Certificate, Integer> CERTIFICATE_QUANTITY = Collections.singletonMap(CERTIFICATE, 2);
    private static final Purchase PURCHASE = new Purchase(ID_VALID, ACCOUNT, CERTIFICATE_QUANTITY, BigDecimal.TEN, NOW);

    @Mock
    private PurchaseRepository purchaseRepository;
    @Mock
    private AccountService accountService;
    @Mock
    private CertificateService certificateService;

    @InjectMocks
    private PurchaseServiceImpl purchaseService;

    @BeforeEach
    public void setUp() {
        //Positive scenario
        lenient().when(purchaseRepository.findById(anyLong())).thenReturn(Optional.of(PURCHASE));
        lenient().when(accountService.findById(anyLong())).thenReturn(ACCOUNT);
        lenient().when(certificateService.findById(anyLong())).thenReturn(CERTIFICATE);
    }

    @Test
    public void findById_WhenIdIsNull_ShouldThrowException() {
        //given
        //when
        //then
        Assertions.assertThrows(NullPointerException.class, () ->
                purchaseService.findById(null)
        );
    }

    @Test
    public void findById_WhenIdInvalid_ShouldThrowException() {
        //given
        //when
        //then
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                purchaseService.findById(ID_INVALID)
        );
    }

    @Test
    public void findById_ShouldFindById() {
        //given
        //when
        Purchase actual = purchaseService.findById(ID_VALID);
        //then
        Assertions.assertEquals(PURCHASE, actual);
    }

    @Test
    public void findById_WhenNotFound_ShouldThrowException() {
        //given
        lenient().when(purchaseRepository.findById(anyLong())).thenReturn(Optional.empty());
        //when
        //then
        Assertions.assertThrows(EntityNotFoundException.class, () ->
                purchaseService.findById(ID_VALID)
        );
    }

    @Test
    public void findPage_WhenPageableIsNull_ShouldThrowException() {
        //given
        Specification<Purchase> specification = new FindAllSpecification<>();
        //when
        //then
        Assertions.assertThrows(NullPointerException.class, () ->
                purchaseService.findPage(null, specification)
        );
    }

    @Test
    public void findPage_WhenSpecificationIsNull_ShouldThrowException() {
        //given
        Pageable pageable = PageRequest.of(1, 10, Sort.unsorted());
        //when
        //then
        Assertions.assertThrows(NullPointerException.class, () ->
                purchaseService.findPage(pageable, null)
        );
    }

    @Test
    public void findPage_ShouldFindPageOfPurchases() {
        //given
        Pageable pageable = PageRequest.of(1, 10, Sort.unsorted());
        Specification<Purchase> specification = new FindAllSpecification<>();
        //when
        purchaseService.findPage(pageable, specification);
        //then
        verify(purchaseRepository).findAll(specification, pageable);
    }

    @Test
    public void createPurchase_WhenPurchaseRequestIsNull_ShouldThrowException() {
        //given
        //when
        //then
        Assertions.assertThrows(NullPointerException.class, () ->
                purchaseService.createPurchase(null)
        );
    }

    @Test
    public void createPurchase_ShouldCreatePurchase() {
        //given
        PurchaseRequest purchaseRequest = new PurchaseRequest(ID_VALID, Collections.singletonMap(ID_VALID, 2));
        when(purchaseRepository.save(any())).then(returnsFirstArg());
        //when
        Purchase actual = purchaseService.createPurchase(purchaseRequest);
        //then
        BigDecimal expectedCost = new BigDecimal(2).multiply(BigDecimal.ONE);
        Purchase expected = new Purchase(null, ACCOUNT, CERTIFICATE_QUANTITY, expectedCost, null);
        Assertions.assertEquals(expected, actual);
    }

}
