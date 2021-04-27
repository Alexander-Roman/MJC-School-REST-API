package com.epam.esm.web.assember;

import com.epam.esm.persistence.entity.Account;
import com.epam.esm.web.model.AccountDto;
import org.springframework.hateoas.server.RepresentationModelAssembler;

public interface AccountDtoAssembler extends RepresentationModelAssembler<Account, AccountDto> {

}
