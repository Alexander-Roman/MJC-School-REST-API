package com.epam.esm.web.exception.model;

import java.io.Serializable;

/**
 * Interface for nested errors representation
 */
public interface NestedError extends Serializable {

    /**
     * Returns error message text
     *
     * @return String message
     */
    String getMessage();

    /**
     * Returns error code as String
     *
     * @return String error code
     */
    String getCode();

}
