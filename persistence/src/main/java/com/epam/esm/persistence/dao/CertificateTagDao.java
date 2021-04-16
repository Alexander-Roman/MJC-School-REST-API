package com.epam.esm.persistence.dao;

import com.epam.esm.persistence.entity.CertificateTag;

import java.util.Collection;
import java.util.List;

/**
 * Common DAO pattern interface for CertificateTag entity
 */
public interface CertificateTagDao {

    /**
     * Finds all CertificateTags that attached to a single certificate specified by id
     *
     * @param certificateId certificate id
     * @return List of CertificateTag found
     */
    List<CertificateTag> findByCertificateId(Long certificateId);

    /**
     * Creates new CertificateTag
     *
     * @param certificateTag to create
     * @return CertificateTag created including auto generated id
     */
    CertificateTag create(CertificateTag certificateTag);

    /**
     * Creates several CertificateTags in one operation
     *
     * @param certificateTags collection of CertificateTags to create
     */
    void create(Collection<CertificateTag> certificateTags);

    /**
     * Deletes several CertificateTags based on collection of ids
     *
     * @param certificateTagIds ids collection of CertificateTags to delete
     */
    void delete(Collection<Long> certificateTagIds);

    /**
     * Deletes all CertificateTags based on its certificate id
     *
     * @param certificateId certificate id of CertificateTags being deleted
     */
    void deleteByCertificateId(Long certificateId);

    /**
     * Deletes all CertificateTags based on its tag id
     *
     * @param tagId tag id of CertificateTags being deleted
     */
    void deleteByTagId(Long tagId);

}
