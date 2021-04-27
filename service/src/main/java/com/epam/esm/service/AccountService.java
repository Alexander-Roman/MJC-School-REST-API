package com.epam.esm.service;

import com.epam.esm.persistence.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AccountService {

    Account findById(Long id);

    Page<Account> findPage(Pageable pageable);

}
