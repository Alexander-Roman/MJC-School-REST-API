package com.epam.esm.service;

import com.epam.esm.persistence.entity.Account;
import com.epam.esm.persistence.entity.Role;
import com.epam.esm.persistence.repository.AccountRepository;
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

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AccountServiceImplTest {

    private static final Long ID_VALID = 1L;
    private static final Long ID_INVALID = -1L;

    private static final Account ACCOUNT = new Account(ID_VALID, "email@mail.com", "password", "name", Role.USER, false);

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    @BeforeEach
    public void setUp() {
        //Positive scenario
        lenient().when(accountRepository.findById(anyLong())).thenReturn(Optional.of(ACCOUNT));
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
        Assertions.assertEquals(ACCOUNT, actual);
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
        verify(accountRepository).find(eq(pageable), any());
    }

}
