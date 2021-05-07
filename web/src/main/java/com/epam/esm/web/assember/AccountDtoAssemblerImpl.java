package com.epam.esm.web.assember;

import com.epam.esm.persistence.entity.Account;
import com.epam.esm.web.controller.AccountController;
import com.epam.esm.web.controller.PurchaseController;
import com.epam.esm.web.mapper.AccountMapper;
import com.epam.esm.web.model.AccountDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class AccountDtoAssemblerImpl extends RepresentationModelAssemblerSupport<Account, AccountDto> implements AccountDtoAssembler {

    private static final String REL_ALL_PURCHASES = "purchases";

    private final AccountMapper accountMapper;

    @Autowired
    public AccountDtoAssemblerImpl(AccountMapper accountMapper) {
        super(AccountController.class, AccountDto.class);
        this.accountMapper = accountMapper;
    }

    @Override
    public AccountDto toModel(Account account) {
        AccountDto accountDto = accountMapper.map(account);
        Long id = accountDto.getId();
        accountDto.add(linkTo(methodOn(AccountController.class).getAccountById(id)).withSelfRel());
        accountDto.add(linkTo(methodOn(PurchaseController.class).getPurchasePage(id, null)).withRel(REL_ALL_PURCHASES));
        return accountDto;
    }

}
