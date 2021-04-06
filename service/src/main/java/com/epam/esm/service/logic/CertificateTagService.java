package com.epam.esm.service.logic;

import com.epam.esm.persistence.entity.Tag;

import java.util.Set;

public interface CertificateTagService {

    void addTagSet(Long certificateId, Set<Tag> tags);

    void updateTagSet(Long certificateId, Set<Tag> tags);

    void deleteByCertificateId(Long id);

    void deleteByTagId(Long id);

}
