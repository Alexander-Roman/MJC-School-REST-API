package com.epam.esm.service.validator;

import com.epam.esm.persistence.model.SortRequest;

public interface SortRequestValidator<T> extends Validator<SortRequest> {

    @Override
    boolean isValid(SortRequest sortRequest);

}
