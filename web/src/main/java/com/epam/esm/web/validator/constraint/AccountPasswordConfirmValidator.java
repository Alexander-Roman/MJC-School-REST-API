package com.epam.esm.web.validator.constraint;

import com.epam.esm.web.model.AccountDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AccountPasswordConfirmValidator implements ConstraintValidator<AccountPasswordConfirm, AccountDto> {

    @Override
    public void initialize(AccountPasswordConfirm constraintAnnotation) {

    }

    @Override
    public boolean isValid(AccountDto accountDto, ConstraintValidatorContext context) {
        String password = accountDto.getPassword();
        String passwordConfirm = accountDto.getPasswordConfirm();
        return password.equals(passwordConfirm);
    }

}
