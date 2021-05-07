package com.epam.esm.web.assember;

import com.epam.esm.persistence.entity.Account;
import com.epam.esm.web.model.AccountDto;
import org.springframework.hateoas.server.RepresentationModelAssembler;

/**
 * Interface for components that convert Account type into AccountDto as RepresentationModel
 */
public interface AccountDtoAssembler extends RepresentationModelAssembler<Account, AccountDto> {

}
