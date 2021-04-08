package com.epam.esm.service.validator;

import com.epam.esm.persistence.model.SortRequest;

public interface CertificateSortRequestValidator extends Validator<SortRequest> {

    @Override
    boolean isValid(SortRequest sortRequest);

}
