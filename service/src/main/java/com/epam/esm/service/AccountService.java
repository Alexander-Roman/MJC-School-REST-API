package com.epam.esm.service;

import com.epam.esm.persistence.entity.Account;
import com.epam.esm.service.exception.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Business logic interface for Accounts
 */
public interface AccountService {

    /**
     * Finds Account by specified ID
     *
     * @param id of Account to find
     * @return Account found
     * @throws NullPointerException     when id is null
     * @throws IllegalArgumentException when id is not valid
     * @throws EntityNotFoundException  when found noting
     */
    Account findById(Long id);

    /**
     * Finds page of Accounts based on pagination parameters.
     *
     * @param pageable object with pagination parameters
     * @return Page of Accounts found
     * @throws NullPointerException when pageable is null
     */
    Page<Account> findPage(Pageable pageable);

}
