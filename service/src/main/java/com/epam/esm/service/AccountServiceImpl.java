package com.epam.esm.service;

import com.epam.esm.persistence.entity.Account;
import com.epam.esm.persistence.repository.AccountRepository;
import com.epam.esm.persistence.specification.FindAllSpecification;
import com.epam.esm.persistence.specification.account.FindByEmailSpecification;
import com.epam.esm.service.exception.AccountExistsException;
import com.epam.esm.service.exception.EntityNotFoundException;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

    private static final String ERROR_MESSAGE_ID_INVALID = "Invalid ID parameter: ";
    private static final String ERROR_MESSAGE_ACCOUNT_NOT_FOUND = "Account does not exists! ID: ";
    private static final long MIN_ID_VALUE = 1L;

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository,
                              PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Account findById(Long id) {
        Preconditions.checkNotNull(id, ERROR_MESSAGE_ID_INVALID + id);
        Preconditions.checkArgument(id >= MIN_ID_VALUE, ERROR_MESSAGE_ID_INVALID + id);

        Optional<Account> account = accountRepository.findById(id);
        return account
                .orElseThrow(() -> new EntityNotFoundException(ERROR_MESSAGE_ACCOUNT_NOT_FOUND + id));
    }

    @Override
    public Page<Account> findPage(Pageable pageable) {
        Preconditions.checkNotNull(pageable, "Pageable argument invalid: " + pageable);

        Specification<Account> specification = new FindAllSpecification<>();
        return accountRepository.findAll(specification, pageable);
    }

    @Override
    public Account create(Account account) {
        Preconditions.checkNotNull(account, "Account invalid: " + account);

        String email = account.getEmail();
        Specification<Account> specification = new FindByEmailSpecification(email);
        Optional<Account> found = accountRepository.findOne(specification);
        if (found.isPresent()) {
            throw new AccountExistsException("Account with specified email already exists: " + email);
        }

        String password = account.getPassword();
        String encoded = passwordEncoder.encode(password);
        Account accountWithPasswordEncoded = Account.Builder
                .from(account)
                .setPassword(encoded)
                .build();
        return accountRepository.save(accountWithPasswordEncoded);
    }

}
