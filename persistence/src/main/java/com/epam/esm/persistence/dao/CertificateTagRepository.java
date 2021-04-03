package com.epam.esm.persistence.dao;

import com.epam.esm.persistence.entity.Certificate;

public interface CertificateTagRepository {

    void resolveAddedTags(Certificate certificate);

    void resolveRemovedTags(Certificate certificate);

    void deleteByCertificateId(Long id);

}
