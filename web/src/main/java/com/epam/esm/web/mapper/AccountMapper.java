package com.epam.esm.web.mapper;

import com.epam.esm.persistence.entity.Account;
import com.epam.esm.web.model.AccountDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface AccountMapper {

    @Mapping(target = "email", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "passwordConfirm", ignore = true)
    AccountDto map(Account account);

    Account map(AccountDto accountDto);

}
