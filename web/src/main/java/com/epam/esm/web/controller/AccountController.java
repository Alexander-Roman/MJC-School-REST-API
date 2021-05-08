package com.epam.esm.web.controller;

import com.epam.esm.persistence.entity.Account;
import com.epam.esm.persistence.entity.Account_;
import com.epam.esm.service.AccountService;
import com.epam.esm.web.assember.AccountDtoAssembler;
import com.epam.esm.web.mapper.AccountMapper;
import com.epam.esm.web.model.AccountDto;
import com.epam.esm.web.validator.constraint.AllowedOrderProperties;
import com.epam.esm.web.validator.group.AccountCreate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import javax.validation.groups.Default;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/accounts")
@Validated
public class AccountController {

    private static final String MSG_CODE_ID_INVALID = "{validation.constraints.id.min}";
    private static final long MIN_ID = 1L;
    private static final String REL_ALL_ACCOUNTS = "accounts";

    private final AccountService accountService;
    private final PagedResourcesAssembler<Account> accountPagedResourcesAssembler;
    private final AccountDtoAssembler accountDtoAssembler;
    private final AccountMapper accountMapper;


    @Autowired
    public AccountController(AccountService accountService,
                             PagedResourcesAssembler<Account> accountPagedResourcesAssembler,
                             AccountDtoAssembler accountDtoAssembler,
                             AccountMapper accountMapper) {
        this.accountService = accountService;
        this.accountPagedResourcesAssembler = accountPagedResourcesAssembler;
        this.accountDtoAssembler = accountDtoAssembler;
        this.accountMapper = accountMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDto> getAccountById(@PathVariable("id") @Min(value = MIN_ID, message = MSG_CODE_ID_INVALID) Long id) {
        Account account = accountService.findById(id);
        AccountDto accountDto = accountDtoAssembler.toModel(account);

        accountDto.add(linkTo(methodOn(AccountController.class).getAccountPage(null)).withRel(REL_ALL_ACCOUNTS));
        return new ResponseEntity<>(accountDto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<PagedModel<AccountDto>> getAccountPage(@AllowedOrderProperties(Account_.NAME) Pageable pageable) {
        Page<Account> accountPage = accountService.findPage(pageable);
        PagedModel<AccountDto> accountDtoPagedModel = accountPagedResourcesAssembler.toModel(accountPage, accountDtoAssembler);
        return new ResponseEntity<>(accountDtoPagedModel, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<AccountDto> createAccount(@RequestBody @Validated({Default.class, AccountCreate.class}) AccountDto accountDto) {
        Account account = accountMapper.map(accountDto);
        Account created = accountService.create(account);
        AccountDto createdDto = accountDtoAssembler.toModel(created);

        createdDto.add(linkTo(methodOn(AccountController.class).getAccountPage(null)).withRel(REL_ALL_ACCOUNTS));
        return new ResponseEntity<>(createdDto, HttpStatus.CREATED);
    }

}
