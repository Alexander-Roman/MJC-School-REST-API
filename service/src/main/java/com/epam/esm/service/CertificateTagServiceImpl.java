package com.epam.esm.service;

import com.epam.esm.persistence.dao.CertificateTagDao;
import com.epam.esm.persistence.entity.CertificateTag;
import com.epam.esm.persistence.entity.Tag;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CertificateTagServiceImpl implements CertificateTagService {

    private static final String ERROR_MESSAGE_ID_INVALID = "Invalid ID parameter: ";
    private static final String ERROR_MESSAGE_TAGS_INVALID = "Invalid tags parameter: ";
    private static final long MIN_ID_VALUE = 1L;

    private final CertificateTagDao certificateTagDao;

    @Autowired
    public CertificateTagServiceImpl(CertificateTagDao certificateTagDao) {
        this.certificateTagDao = certificateTagDao;
    }

    @Override
    @Transactional
    public void addTags(Long certificateId, Set<Tag> tags) {
        Preconditions.checkNotNull(tags, ERROR_MESSAGE_TAGS_INVALID + tags);
        Preconditions.checkNotNull(certificateId, ERROR_MESSAGE_ID_INVALID + certificateId);
        Preconditions.checkArgument(certificateId >= MIN_ID_VALUE, ERROR_MESSAGE_ID_INVALID + certificateId);

        if (tags.isEmpty()) {
            return;
        }

        List<CertificateTag> certificateTags = this.createCertificateTags(certificateId, tags);
        certificateTagDao.create(certificateTags);
    }

    @Override
    @Transactional
    public void updateTags(Long certificateId, Set<Tag> tags) {
        Preconditions.checkNotNull(tags, ERROR_MESSAGE_TAGS_INVALID + tags);
        Preconditions.checkNotNull(certificateId, ERROR_MESSAGE_ID_INVALID + certificateId);
        Preconditions.checkArgument(certificateId >= MIN_ID_VALUE, ERROR_MESSAGE_ID_INVALID + certificateId);

        List<CertificateTag> newCertificateTags = this.createCertificateTags(certificateId, tags);

        List<CertificateTag> oldCertificateTags = certificateTagDao.findByCertificateId(certificateId);
        List<Long> oldTagIds = oldCertificateTags
                .stream()
                .map(CertificateTag::getTagId)
                .collect(Collectors.toList());
        List<CertificateTag> toCreate = newCertificateTags
                .stream()
                .filter(certificateTag -> !oldTagIds.contains(certificateTag.getTagId()))
                .collect(Collectors.toList());
        if (!toCreate.isEmpty()) {
            certificateTagDao.create(toCreate);
        }

        List<Long> newTagIds = newCertificateTags
                .stream()
                .map(CertificateTag::getTagId)
                .collect(Collectors.toList());
        List<Long> toDelete = oldCertificateTags
                .stream()
                .filter(certificateTag -> !newTagIds.contains(certificateTag.getTagId()))
                .map(CertificateTag::getId)
                .collect(Collectors.toList());
        if (!toDelete.isEmpty()) {
            certificateTagDao.delete(toDelete);
        }

    }

    @Override
    public void deleteByCertificateId(Long id) {
        Preconditions.checkNotNull(id, ERROR_MESSAGE_ID_INVALID + id);
        Preconditions.checkArgument(id >= MIN_ID_VALUE, ERROR_MESSAGE_ID_INVALID + id);

        certificateTagDao.deleteByCertificateId(id);
    }

    @Override
    public void deleteByTagId(Long id) {
        Preconditions.checkNotNull(id, ERROR_MESSAGE_ID_INVALID + id);
        Preconditions.checkArgument(id >= MIN_ID_VALUE, ERROR_MESSAGE_ID_INVALID + id);

        certificateTagDao.deleteByTagId(id);
    }

    private List<CertificateTag> createCertificateTags(Long certificateId, Set<Tag> tags) {
        return tags
                .stream()
                .map(tag -> new CertificateTag(null, certificateId, tag.getId()))
                .collect(Collectors.toList());
    }

}
