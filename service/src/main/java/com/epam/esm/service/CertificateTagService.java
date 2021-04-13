package com.epam.esm.service;

import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.service.exception.ServiceException;

import java.util.Set;

/**
 * Business logic interface for CertificateTags
 */
public interface CertificateTagService {

    /**
     * Creates new CertificateTags based on tag set of single certificate.
     * Use this method when you a sure, that meaning relationships are still not exist
     * Tags should have id to create valid CertificateTags
     *
     * @param certificateId id of target certificate to create CertificateTags
     * @param tags          tag set of target certificate to create CertificateTags
     * @throws NullPointerException     when one of parameters is null
     * @throws IllegalArgumentException when certificateId is not valid
     * @throws ServiceException         when one of resulting CertificateTags invalid
     */
    void addTags(Long certificateId, Set<Tag> tags);

    /**
     * Manages CertificateTags to match a given tag set for target certificate
     * Creates and/or deletes CertificateTags if necessary.
     * Tags should have id to create valid CertificateTags
     *
     * @param certificateId target certificate id
     * @param tags          expected tag set of target certificate
     * @throws NullPointerException     when one of parameters is null
     * @throws IllegalArgumentException when certificateId is not valid
     * @throws ServiceException         when one of created CertificateTags invalid
     */
    void updateTags(Long certificateId, Set<Tag> tags);

    /**
     * Deletes all CertificateTags matching specified certificate id
     *
     * @param id certificateId of CertificateTags being deleted
     * @throws NullPointerException     when id is null
     * @throws IllegalArgumentException when id is not valid
     */
    void deleteByCertificateId(Long id);

    /**
     * Deletes all CertificateTags matching specified tag id
     *
     * @param id tagId of CertificateTags being deleted
     * @throws NullPointerException     when id is null
     * @throws IllegalArgumentException when id is not valid
     */
    void deleteByTagId(Long id);

}
