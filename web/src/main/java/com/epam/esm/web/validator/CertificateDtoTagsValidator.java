package com.epam.esm.web.validator;

import com.epam.esm.web.model.TagDto;
import org.springframework.lang.NonNull;
import org.springframework.validation.Validator;

public interface CertificateDtoTagsValidator extends Validator {

    @Override
    default boolean supports(@NonNull Class<?> clazz) {
        return TagDto.class.equals(clazz);
    }

}
