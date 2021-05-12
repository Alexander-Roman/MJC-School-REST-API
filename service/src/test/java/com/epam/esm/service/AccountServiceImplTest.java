package com.epam.esm.service;

import com.epam.esm.persistence.entity.Account;
import com.epam.esm.persistence.entity.Role;
import com.epam.esm.persistence.repository.AccountRepository;
import com.epam.esm.service.exception.AccountExistsException;
import com.epam.esm.service.exception.EntityNotFoundException;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AccountServiceImplTest {

    private static final Long ID_VALID = 1L;
    private static final Long ID_INVALID = -1L;

    private static final Account ACCOUNT_WITH_ID = new Account(ID_VALID, "email@mail.com", "password", "name", Role.USER, false);
    private static final Account ACCOUNT_WITHOUT_ID = new Account(null, "email@mail.com", "password", "name", Role.USER, false);

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AccountServiceImpl accountService;

    @BeforeEach
    public void setUp() {
        //Positive scenario
        lenient().when(accountRepository.findById(anyLong())).thenReturn(Optional.of(ACCOUNT_WITH_ID));
        lenient().when(accountRepository.findOne(any(Specification.class))).thenReturn(Optional.of(ACCOUNT_WITH_ID));
        lenient().when(accountRepository.save(any())).thenReturn(ACCOUNT_WITH_ID);
    }

    @Test
    public void findById_WhenIdIsNull_ShouldThrowException() {
        //given
        //when
        //then
        Assertions.assertThrows(NullPointerException.class, () ->
                accountService.findById(null)
        );
    }

    @Test
    public void findById_WhenIdInvalid_ShouldThrowException() {
        //given
        //when
        //then
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                accountService.findById(ID_INVALID)
        );
    }

    @Test
    public void findById_ShouldFindById() {
        //given
        //when
        Account actual = accountService.findById(ID_VALID);
        //then
        Assertions.assertEquals(ACCOUNT_WITH_ID, actual);
    }

    @Test
    public void findById_WhenNotFound_ShouldThrowException() {
        //given
        lenient().when(accountRepository.findById(anyLong())).thenReturn(Optional.empty());
        //when
        //then
        Assertions.assertThrows(EntityNotFoundException.class, () ->
                accountService.findById(ID_VALID)
        );
    }

    @Test
    public void findPage_WhenPageableIsNull_ShouldThrowException() {
        //given
        //when
        //then
        Assertions.assertThrows(NullPointerException.class, () ->
                accountService.findPage(null)
        );
    }

    @Test
    public void findPage_ShouldFindPageOfAccounts() {
        //given
        Pageable pageable = PageRequest.of(1, 10, Sort.unsorted());
        //when
        accountService.findPage(pageable);
        //then
        verify(accountRepository).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    public void create_WhenAccountIsNull_ShouldThrowException() {
        //given
        //when
        //then
        Assertions.assertThrows(NullPointerException.class, () ->
                accountService.create(null)
        );
    }

    @Test
    public void create_WhenAccountExists_ShouldThrowException() {
        //given
        //when
        //then
        Assertions.assertThrows(AccountExistsException.class, () ->
                accountService.create(ACCOUNT_WITHOUT_ID)
        );
    }

    @Test
    public void create_WhenAccountNotExists_ShouldSaveWithPasswordEncoded() {
        //given
        lenient().when(accountRepository.findOne(any(Specification.class))).thenReturn(Optional.empty());
        lenient().when(passwordEncoder.encode(anyString())).thenReturn("passwordEncoded");
        //when
        accountService.create(ACCOUNT_WITHOUT_ID);
        //then
        Account expected = Account.Builder
                .from(ACCOUNT_WITHOUT_ID)
                .setPassword("passwordEncoded")
                .build();
        verify(accountRepository).save(expected);

    }

}
