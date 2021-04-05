package com.epam.esm.persistence.dao;

import com.epam.esm.persistence.entity.Certificate;

public interface CertificateTagDao {

    void resolveAddedTagsByNames(Certificate certificate);

    void resolveRemovedTagsByNames(Certificate certificate);

    void deleteByCertificateId(Long id);

    void deleteByTagId(Long id);

}
