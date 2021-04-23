package com.epam.esm.service;

import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.service.exception.EntityNotFoundException;
import com.epam.esm.service.exception.ServiceException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

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

    Page<Certificate> findPage(Pageable pageable, Specification<Certificate> specification);

    /**
     * Creates new certificate
     *
     * @param certificate to create
     * @return Certificate created in effect
     * @throws NullPointerException when certificate parameter is null
     * @throws ServiceException     when certificate invalid
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
