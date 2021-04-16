package com.epam.esm.service.validator;

/**
 * Predicate validator interface
 *
 * @param <T> type of supported object
 */
public interface Validator<T> {

    /**
     * Validates object and returns true if valid, otherwise returns false
     *
     * @param object to validate
     * @return true - if valid, false - if not valid
     */
    boolean isValid(T object);

}
