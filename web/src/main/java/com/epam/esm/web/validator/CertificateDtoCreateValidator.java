package com.epam.esm.web.validator;

import com.epam.esm.web.model.CertificateDto;
import org.springframework.lang.NonNull;
import org.springframework.validation.Validator;

/**
 * Extension of Spring Validator interface for CertificateDto object in context of creating
 */
public interface CertificateDtoCreateValidator extends Validator {

    @Override
    default boolean supports(@NonNull Class<?> clazz) {
        return CertificateDto.class.equals(clazz);
    }

}
