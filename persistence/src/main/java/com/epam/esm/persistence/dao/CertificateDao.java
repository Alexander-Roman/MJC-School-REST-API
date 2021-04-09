package com.epam.esm.persistence.dao;

import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.persistence.model.FilterRequest;
import com.epam.esm.persistence.model.SortRequest;

import java.util.List;
import java.util.Optional;

/**
 * Common DAO pattern interface for Certificate entity
 */
public interface CertificateDao {


    /**
     * Finds and returns certificate (with tags) by specified id
     *
     * @param id of certificate to find
     * @return Optional of certificate found or empty if found nothing
     */
    Optional<Certificate> findById(Long id);

    /**
     * Finds and returns all certificates with tags
     *
     * @return List of all certificates
     */
    List<Certificate> findAll();

    /**
     * Finds and returns a list of certificates (with tags) based on filtering and sorting options
     *
     * @param sortRequest   an object that encapsulates sorting options
     * @param filterRequest an object that encapsulates filtering options
     * @return sorted List of certificates found
     */
    List<Certificate> findAll(SortRequest sortRequest, FilterRequest filterRequest);

    /**
     * Creates new certificate. Certificate tags are not involved in operation
     *
     * @param certificate to create
     * @return certificate created including auto generated id
     */
    Certificate create(Certificate certificate);

    /**
     * Updates existing certificate based on specified id. Certificate tags are not involved in operation
     *
     * @param certificate updated version of certificate to save
     * @return certificate updated including auto generated values (if any)
     */
    Certificate update(Certificate certificate);

    /**
     * Deletes certificate by specified id. Certificate tags are not involved in operation
     *
     * @param id of certificate to delete
     */
    void delete(Long id);

}
