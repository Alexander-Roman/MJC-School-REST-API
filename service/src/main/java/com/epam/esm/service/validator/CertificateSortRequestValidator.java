package com.epam.esm.service.validator;

import com.epam.esm.persistence.model.SortRequest;

/**
 * Predicate validator for SortRequest in the context of the certificate
 */
public interface CertificateSortRequestValidator extends Validator<SortRequest> {

    /**
     * Validates SortRequest and returns true if valid for Certificate, otherwise returns false
     *
     * @param sortRequest to validate
     * @return true - if valid, false - if not valid
     */
    @Override
    boolean isValid(SortRequest sortRequest);

}
