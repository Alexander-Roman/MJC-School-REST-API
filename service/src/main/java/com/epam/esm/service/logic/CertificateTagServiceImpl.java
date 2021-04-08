package com.epam.esm.service.logic;

import com.epam.esm.persistence.dao.CertificateTagDao;
import com.epam.esm.persistence.entity.CertificateTag;
import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.validator.Validator;
import com.google.common.base.Preconditions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CertificateTagServiceImpl implements CertificateTagService {

    public static final long MIN_ID_VALUE = 1L;

    private final CertificateTagDao certificateTagDao;
    private final Validator<CertificateTag> certificateTagValidator;

    public CertificateTagServiceImpl(CertificateTagDao certificateTagDao,
                                     Validator<CertificateTag> certificateTagValidator) {
        this.certificateTagDao = certificateTagDao;
        this.certificateTagValidator = certificateTagValidator;
    }

    @Override
    @Transactional
    public void addTagSet(Long certificateId, Set<Tag> tags) {
        Preconditions.checkNotNull(tags, "Invalid tags parameter: " + tags);
        Preconditions.checkNotNull(certificateId, "Invalid ID parameter: " + certificateId);
        Preconditions.checkArgument(certificateId >= MIN_ID_VALUE, "Invalid ID parameter: " + certificateId);

        if (tags.isEmpty()) {
            return;
        }

        List<CertificateTag> certificateTags = tags
                .stream()
                .map(tag -> new CertificateTag(null, certificateId, tag.getId()))
                .collect(Collectors.toList());

        for (CertificateTag certificateTag : certificateTags) {
            if (!certificateTagValidator.isValid(certificateTag)) {
                throw new ServiceException("CertificateTag invalid: " + certificateTag);
            }
        }
        certificateTagDao.create(certificateTags);
    }

    @Override
    @Transactional
    public void updateTagSet(Long certificateId, Set<Tag> tags) {
        Preconditions.checkNotNull(tags, "Invalid tags parameter: " + tags);
        Preconditions.checkNotNull(certificateId, "Invalid ID parameter: " + certificateId);
        Preconditions.checkArgument(certificateId >= MIN_ID_VALUE, "Invalid ID parameter: " + certificateId);

        List<CertificateTag> newCertificateTags = tags
                .stream()
                .map(tag -> new CertificateTag(null, certificateId, tag.getId()))
                .collect(Collectors.toList());
        for (CertificateTag certificateTag : newCertificateTags) {
            if (!certificateTagValidator.isValid(certificateTag)) {
                throw new ServiceException("CertificateTag invalid: " + certificateTag);
            }
        }

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
        Preconditions.checkNotNull(id, "Invalid ID parameter: " + id);
        Preconditions.checkArgument(id >= MIN_ID_VALUE, "Invalid ID parameter: " + id);

        certificateTagDao.deleteByCertificateId(id);
    }

    @Override
    public void deleteByTagId(Long id) {
        Preconditions.checkNotNull(id, "Invalid ID parameter: " + id);
        Preconditions.checkArgument(id >= MIN_ID_VALUE, "Invalid ID parameter: " + id);

        certificateTagDao.deleteByTagId(id);
    }

}
