package com.epam.esm.service.logic;

import com.epam.esm.persistence.dao.CertificateDao;
import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.service.exception.EntityNotFoundException;
import com.epam.esm.persistence.model.FilterRequest;
import com.epam.esm.persistence.model.SortRequest;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.validator.CertificateSortRequestValidator;
import com.epam.esm.service.validator.Validator;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CertificateServiceImpl implements CertificateService {

    public static final long MIN_ID_VALUE = 1L;

    private final CertificateDao certificateDao;
    private final TagService tagService;
    private final CertificateTagService certificateTagService;
    private final Validator<Certificate> certificateValidator;
    private final CertificateSortRequestValidator certificateSortRequestValidator;

    @Autowired
    public CertificateServiceImpl(CertificateDao certificateDao,
                                  TagService tagService,
                                  CertificateTagService certificateTagService,
                                  Validator<Certificate> certificateValidator,
                                  CertificateSortRequestValidator certificateSortRequestValidator) {
        this.certificateDao = certificateDao;
        this.tagService = tagService;
        this.certificateTagService = certificateTagService;
        this.certificateValidator = certificateValidator;
        this.certificateSortRequestValidator = certificateSortRequestValidator;
    }

    @Override
    public Certificate findById(Long id) {
        Preconditions.checkNotNull(id, "Invalid ID parameter: " + id);
        Preconditions.checkArgument(id >= MIN_ID_VALUE, "Invalid ID parameter: " + id);

        Optional<Certificate> certificate = certificateDao.findById(id);
        return certificate
                .orElseThrow(() -> new EntityNotFoundException("Certificate does not exists! ID: " + id));
    }

    @Override
    public List<Certificate> findAll(SortRequest sortRequest, FilterRequest filterRequest) {
        Preconditions.checkNotNull(filterRequest, "Invalid FilterRequest parameter: " + filterRequest);

        if (!certificateSortRequestValidator.isValid(sortRequest)) {
            throw new ServiceException("Invalid SortRequest parameter: " + sortRequest);
        }
        return certificateDao.findAll(sortRequest, filterRequest);
    }

    @Override
    @Transactional
    public Certificate create(Certificate certificate) {
        Preconditions.checkNotNull(certificate, "Certificate invalid: " + certificate);

        Long id = certificate.getId();
        if (id != null) {
            throw new ServiceException("Specifying ids is now allowed for new certificates!");
        }

        LocalDateTime now = LocalDateTime.now();
        Certificate dated = Certificate.Builder
                .from(certificate)
                .setCreateDate(now)
                .setLastUpdateDate(now)
                .build();

        if (!certificateValidator.isValid(dated)) {
            throw new ServiceException("Certificate invalid: " + dated);
        }
        Certificate savedCertificate = certificateDao.create(dated);

        Set<Tag> tags = certificate.getTags();
        if (!tags.isEmpty()) {
            Set<Tag> savedTags = tagService.createIfNotExist(tags);
            savedCertificate = Certificate.Builder
                    .from(savedCertificate)
                    .setTags(savedTags)
                    .build();
            Long generatedId = savedCertificate.getId();
            certificateTagService.addTagSet(generatedId, savedTags);
        }
        return savedCertificate;
    }

    @Override
    @Transactional
    public Certificate selectiveUpdate(Certificate source) {
        Preconditions.checkNotNull(source, "Certificate invalid: " + source);
        Long id = source.getId();
        if (id == null || id < MIN_ID_VALUE) {
            throw new ServiceException("Unable to update certificate! Id is not specified or invalid: " + id);
        }
        Certificate target = certificateDao
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Certificate does not exists! ID: " + id));

        String sourceName = source.getName();
        String sourceDescription = source.getDescription();
        BigDecimal sourcePrice = source.getPrice();
        Integer sourceDuration = source.getDuration();
        Set<Tag> sourceTags = source.getTags();

        String name = sourceName == null
                ? target.getName()
                : sourceName;
        String description = sourceDescription == null
                ? target.getDescription()
                : sourceDescription;
        BigDecimal price = sourcePrice == null
                ? target.getPrice()
                : sourcePrice;
        Integer duration = sourceDuration == null
                ? target.getDuration()
                : sourceDuration;
        Set<Tag> tags = sourceTags == null
                ? target.getTags()
                : sourceTags;

        Certificate certificate = Certificate.Builder
                .from(target)
                .setName(name)
                .setDescription(description)
                .setPrice(price)
                .setDuration(duration)
                .setTags(tags)
                .build();
        return this.update(certificate);
    }

    private Certificate update(Certificate certificate) {
        if (!certificateValidator.isValid(certificate)) {
            throw new ServiceException("Certificate invalid: " + certificate);
        }
        LocalDateTime now = LocalDateTime.now();
        Certificate dated = Certificate.Builder
                .from(certificate)
                .setLastUpdateDate(now)
                .build();
        Certificate updated = certificateDao.update(dated);

        Set<Tag> tags = certificate.getTags();
        Set<Tag> savedTags = tagService.createIfNotExist(tags);
        Certificate updatedWithTags = Certificate.Builder
                .from(updated)
                .setTags(savedTags)
                .build();

        Long id = updatedWithTags.getId();
        certificateTagService.updateTagSet(id, savedTags);
        return updatedWithTags;
    }

    @Override
    @Transactional
    public Certificate deleteById(Long id) {
        Preconditions.checkNotNull(id, "Invalid ID parameter: " + id);
        Preconditions.checkArgument(id >= MIN_ID_VALUE, "Invalid ID parameter: " + id);
        Certificate target = certificateDao
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Certificate does not exists! ID: " + id));
        certificateTagService.deleteByCertificateId(id);
        certificateDao.delete(id);
        return target;
    }

}
