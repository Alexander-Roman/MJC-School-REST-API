package com.epam.esm.web.controller;

import com.epam.esm.persistence.entity.Account;
import com.epam.esm.persistence.entity.Account_;
import com.epam.esm.persistence.entity.Purchase;
import com.epam.esm.persistence.entity.Purchase_;
import com.epam.esm.service.AccountService;
import com.epam.esm.web.assember.AccountDtoAssembler;
import com.epam.esm.web.mapper.AccountMapper;
import com.epam.esm.web.model.AccountDto;
import com.epam.esm.web.model.PurchaseDto;
import com.epam.esm.web.validator.constraint.AllowedOrderProperties;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/accounts")
@Validated
public class AccountController {

    private static final String MSG_CODE_ID_INVALID = "id.invalid";
    private static final String MSG_SORT_INVALID = "sort.invalid";
    private static final long MIN_ID = 1L;
    private static final String REL_ALL_ACCOUNTS = "accounts";

    private final AccountService accountService;
    private final AccountMapper accountMapper;
    private final PagedResourcesAssembler<Account> accountPagedResourcesAssembler;
    private final AccountDtoAssembler accountDtoAssembler;

    @Autowired
    public AccountController(AccountService accountService,
                             AccountMapper accountMapper,
                             PagedResourcesAssembler<Account> accountPagedResourcesAssembler,
                             AccountDtoAssembler accountDtoAssembler) {
        this.accountService = accountService;
        this.accountMapper = accountMapper;
        this.accountPagedResourcesAssembler = accountPagedResourcesAssembler;
        this.accountDtoAssembler = accountDtoAssembler;
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDto> getAccountById(
            @PathVariable("id")
            @Min(value = MIN_ID, message = MSG_CODE_ID_INVALID) Long id) {
        Account account = accountService.findById(id);
        AccountDto accountDto = accountMapper.map(account);

        accountDto.add(linkTo(methodOn(AccountController.class).getAccountById(id)).withSelfRel());
//        accountDto.add(linkTo(methodOn(AccountController.class).getAccountPage(null, null)).withRel(REL_ALL_ACCOUNTS));
        //orders
        return new ResponseEntity<>(accountDto, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<PagedModel<AccountDto>> getAccountPage(
            @AllowedOrderProperties(message = MSG_SORT_INVALID, value = Account_.NAME) Pageable pageable) {

        Page<Account> accountPage = accountService.findPage(pageable);
        PagedModel<AccountDto> accountDtoPagedModel = accountPagedResourcesAssembler.toModel(accountPage, accountDtoAssembler);
        return new ResponseEntity<>(accountDtoPagedModel, HttpStatus.OK);
    }

//    @GetMapping("/{id}/purchases")
//    public ResponseEntity<PagedModel<PurchaseDto>> getAccountPurchasePage(
//            @PathVariable("id")
//            @Min(value = MIN_ID, message = MSG_CODE_ID_INVALID) Long id,
//            @AllowedOrderProperties(message = MSG_SORT_INVALID, value = {Purchase_.COST, Purchase_.DATE}) Pageable pageable) {
//
//        Purchase
//        Page<Account> accountPage = accountService.findPage(pageable);
//        PagedModel<AccountDto> accountDtoPagedModel = accountPagedResourcesAssembler.toModel(accountPage, accountDtoAssembler);
//        return new ResponseEntity<>(accountDtoPagedModel, HttpStatus.OK);
//    }

}
