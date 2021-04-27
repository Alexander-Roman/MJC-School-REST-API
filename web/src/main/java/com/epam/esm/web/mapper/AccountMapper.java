package com.epam.esm.web.mapper;

import com.epam.esm.persistence.entity.Account;
import com.epam.esm.web.model.AccountDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface AccountMapper {

    AccountDto map(Account account);

}
