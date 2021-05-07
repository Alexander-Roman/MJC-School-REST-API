package com.epam.esm.web.exception.model;

import java.io.Serializable;

/**
 * Main interface for errors representation
 */
public interface RootError extends Serializable {

    /**
     * Returns error message text
     *
     * @return String message
     */
    String getMessage();

    /**
     * Returns error code as Integer
     *
     * @return Integer error code
     */
    Integer getCode();

}
