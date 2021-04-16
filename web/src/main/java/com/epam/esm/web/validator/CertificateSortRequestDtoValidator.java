package com.epam.esm.web.validator;

import com.epam.esm.web.model.SortRequestDto;
import org.springframework.lang.NonNull;
import org.springframework.validation.Validator;

/**
 * Extension of Spring Validator interface for SortRequest object in context of sorting Certificates
 */
public interface CertificateSortRequestDtoValidator extends Validator {

    @Override
    default boolean supports(@NonNull Class<?> clazz) {
        return SortRequestDto.class.equals(clazz);
    }

}
