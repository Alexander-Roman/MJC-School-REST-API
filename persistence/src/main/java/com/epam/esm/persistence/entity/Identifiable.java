package com.epam.esm.persistence.entity;

/**
 * Common interface for entities supported by the abstract repository
 */
public interface Identifiable {

    /**
     * Returns ID as Long value
     *
     * @return Long ID
     */
    Long getId();

}
