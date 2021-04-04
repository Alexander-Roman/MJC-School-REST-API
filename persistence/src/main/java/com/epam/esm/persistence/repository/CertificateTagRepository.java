package com.epam.esm.persistence.repository;

import com.epam.esm.persistence.entity.Certificate;

public interface CertificateTagRepository {

    void resolveAddedTagsByNames(Certificate certificate);

    void resolveRemovedTagsByNames(Certificate certificate);

    void deleteByCertificateId(Long id);

    void deleteByTagId(Long id);

}
