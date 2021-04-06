package com.epam.esm.persistence.dao;

import com.epam.esm.persistence.entity.CertificateTag;

import java.util.Collection;
import java.util.List;

public interface CertificateTagDao {

    List<CertificateTag> findByCertificateId(Long certificateId);

    CertificateTag create(CertificateTag certificateTag);

    void create(Collection<CertificateTag> certificateTags);

    void delete(Collection<Long> certificateTagIds);

    void deleteByCertificateId(Long certificateId);

    void deleteByTagId(Long tagId);

}
