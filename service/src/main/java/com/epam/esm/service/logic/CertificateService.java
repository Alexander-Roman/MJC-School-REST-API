package com.epam.esm.service.logic;

import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.persistence.model.FilterRequest;
import com.epam.esm.persistence.model.SortRequest;
import com.epam.esm.service.exception.EntityNotFoundException;
import com.epam.esm.service.exception.ServiceException;

import java.util.List;

/**
 * Business logic interface for Certificates
 */
public interface CertificateService {

    /**
     * Finds certificate by specified id
     *
     * @param id of certificate to find
     * @return Certificate found
     * @throws NullPointerException     when id is null
     * @throws IllegalArgumentException when id is not valid
     * @throws EntityNotFoundException  when found noting
     */
    Certificate findById(Long id);

    /**
     * Finds and returns a list of certificates based on filtering and sorting options
     *
     * @param sortRequest   an object that encapsulates sorting options
     * @param filterRequest an object that encapsulates filtering options
     * @return sorted List of certificates found
     * @throws NullPointerException    when sortRequest or filterRequest is null
     * @throws ServiceException        when sortRequest is not valid for Certificate entity
     * @throws EntityNotFoundException if found noting
     */
    List<Certificate> findAll(SortRequest sortRequest, FilterRequest filterRequest);

    /**
     * Creates new certificate
     *
     * @param certificate to create
     * @return Certificate created in effect
     * @throws NullPointerException when certificate parameter is null
     * @throws ServiceException     when certificate id specified, or certificate invalid
     */
    Certificate create(Certificate certificate);

    /**
     * Updates existing certificate based on specified id.
     * <p>
     * This method accepts inconsistent certificate instances and perform update only with fields than is not null!
     *
     * @param certificate to update
     * @return Certificate updated consistent instance
     * @throws NullPointerException    when certificate parameter is null
     * @throws ServiceException        when certificate id not specified
     * @throws EntityNotFoundException when updating certificate does not exists, or resulting certificate is vot valid
     */
    Certificate selectiveUpdate(Certificate certificate);

    /**
     * Deletes certificate by id, returns successfully deleted certificate
     *
     * @param id of certificate to delete
     * @return Certificate than was deleted in effect
     * @throws NullPointerException     when id is null
     * @throws IllegalArgumentException when id is not valid
     * @throws EntityNotFoundException  when deleting certificate not found
     */
    Certificate deleteById(Long id);

}
