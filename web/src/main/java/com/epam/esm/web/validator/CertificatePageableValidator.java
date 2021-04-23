package com.epam.esm.web.validator;

import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.validation.Validator;

public interface CertificatePageableValidator extends Validator {

    @Override
    default boolean supports(@NonNull Class<?> clazz) {
        return Pageable.class.equals(clazz);
    }

}
