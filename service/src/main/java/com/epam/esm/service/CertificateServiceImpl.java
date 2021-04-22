package com.epam.esm.service;

import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.persistence.model.FilterRequest;
import com.epam.esm.persistence.model.SortRequest;
import com.epam.esm.persistence.repository.CertificateRepository;
import com.epam.esm.service.exception.EntityNotFoundException;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.validator.CertificateSortRequestValidator;
import com.epam.esm.service.validator.Validator;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CertificateServiceImpl implements CertificateService {

    private static final String ERROR_MESSAGE_ID_INVALID = "Invalid ID parameter: ";
    private static final String ERROR_MESSAGE_CERTIFICATE_NOT_FOUND = "Certificate does not exists! ID: ";
    private static final String ERROR_MESSAGE_CERTIFICATE_INVALID = "Certificate invalid: ";
    private static final String ERROR_MESSAGE_FILTER_REQUEST_INVALID = "Invalid FilterRequest parameter: ";
    private static final String ERROR_MESSAGE_SORT_REQUEST_INVALID = "Invalid SortRequest parameter: ";
    private static final long MIN_ID_VALUE = 1L;

    private final CertificateRepository certificateRepository;
    private final TagService tagService;
    private final Validator<Certificate> certificateValidator;
    private final CertificateSortRequestValidator certificateSortRequestValidator;

    @Autowired
    public CertificateServiceImpl(CertificateRepository certificateRepository,
                                  TagService tagService,
                                  Validator<Certificate> certificateValidator,
                                  CertificateSortRequestValidator certificateSortRequestValidator) {
        this.certificateRepository = certificateRepository;
        this.tagService = tagService;
        this.certificateValidator = certificateValidator;
        this.certificateSortRequestValidator = certificateSortRequestValidator;
    }

    @Override
    public Certificate findById(Long id) {
        Preconditions.checkNotNull(id, ERROR_MESSAGE_ID_INVALID + id);
        Preconditions.checkArgument(id >= MIN_ID_VALUE, ERROR_MESSAGE_ID_INVALID + id);

        Optional<Certificate> certificate = certificateRepository.findById(id);
        return certificate
                .orElseThrow(() -> new EntityNotFoundException(ERROR_MESSAGE_CERTIFICATE_NOT_FOUND + id));
    }

    @Override
    public List<Certificate> findAll(SortRequest sortRequest, FilterRequest filterRequest) {
        Preconditions.checkNotNull(filterRequest, ERROR_MESSAGE_FILTER_REQUEST_INVALID + filterRequest);
        Preconditions.checkNotNull(sortRequest, ERROR_MESSAGE_SORT_REQUEST_INVALID + sortRequest);

        if (!certificateSortRequestValidator.isValid(sortRequest)) {
            throw new ServiceException(ERROR_MESSAGE_SORT_REQUEST_INVALID + sortRequest);
        }
        return certificateRepository.findAll(sortRequest, filterRequest);
    }

    @Override
    @Transactional
    public Certificate create(Certificate certificate) {
        Preconditions.checkNotNull(certificate, ERROR_MESSAGE_CERTIFICATE_INVALID + certificate);

        LocalDateTime now = LocalDateTime.now();
        Certificate dated = Certificate.Builder
                .from(certificate)
                .setCreateDate(now)
                .setLastUpdateDate(now)
                .build();

        if (!certificateValidator.isValid(dated)) {
            throw new ServiceException(ERROR_MESSAGE_CERTIFICATE_INVALID + dated);
        }

        Set<Tag> tags = certificate.getTags();
        if (!tags.isEmpty()) {
            Set<Tag> consistentTags = tagService.createIfNotExist(tags);
            dated = Certificate.Builder
                    .from(dated)
                    .setTags(consistentTags)
                    .build();
        }

        return certificateRepository.save(dated);
    }

    @Override
    @Transactional
    public Certificate selectiveUpdate(Certificate source) {
        Preconditions.checkNotNull(source, ERROR_MESSAGE_CERTIFICATE_INVALID + source);
        Long id = source.getId();
        if (id == null || id < MIN_ID_VALUE) {
            throw new ServiceException("Unable to update certificate! Id is not specified or invalid: " + id);
        }
        Certificate target = certificateRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ERROR_MESSAGE_CERTIFICATE_NOT_FOUND + id));

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
            throw new ServiceException(ERROR_MESSAGE_CERTIFICATE_INVALID + certificate);
        }
        LocalDateTime now = LocalDateTime.now();
        Certificate dated = Certificate.Builder
                .from(certificate)
                .setLastUpdateDate(now)
                .build();

        Set<Tag> tags = certificate.getTags();
        Set<Tag> consistentTags = tagService.createIfNotExist(tags);
        Certificate updatedWithTags = Certificate.Builder
                .from(dated)
                .setTags(consistentTags)
                .build();
        return certificateRepository.save(updatedWithTags);
    }

    @Override
    @Transactional
    public Certificate deleteById(Long id) {
        Preconditions.checkNotNull(id, ERROR_MESSAGE_ID_INVALID + id);
        Preconditions.checkArgument(id >= MIN_ID_VALUE, ERROR_MESSAGE_ID_INVALID + id);
        Certificate target = certificateRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ERROR_MESSAGE_CERTIFICATE_NOT_FOUND + id));
        certificateRepository.delete(id);
        return target;
    }

    @Override
    public Page<Certificate> findPage(Pageable pageable, Specification<Certificate> filterRequestSpecification) {
        return certificateRepository.find(pageable, filterRequestSpecification);
    }

}
