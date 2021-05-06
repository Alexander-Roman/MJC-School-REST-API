package com.epam.esm.service;

import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.persistence.repository.CertificateRepository;
import com.epam.esm.persistence.specification.certificare.FindNotDeletedByIdSpecification;
import com.epam.esm.service.exception.EntityNotFoundException;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.validator.Validator;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

@Service
public class CertificateServiceImpl implements CertificateService {

    private static final String ERROR_MESSAGE_ID_INVALID = "Invalid ID parameter: ";
    private static final String ERROR_MESSAGE_CERTIFICATE_NOT_FOUND = "Certificate does not exists! ID: ";
    private static final String ERROR_MESSAGE_CERTIFICATE_INVALID = "Certificate invalid: ";
    private static final long MIN_ID_VALUE = 1L;

    private final CertificateRepository certificateRepository;
    private final TagService tagService;
    private final Validator<Certificate> certificateValidator;

    @Autowired
    public CertificateServiceImpl(CertificateRepository certificateRepository,
                                  TagService tagService,
                                  Validator<Certificate> certificateValidator) {
        this.certificateRepository = certificateRepository;
        this.tagService = tagService;
        this.certificateValidator = certificateValidator;
    }

    @Override
    public Certificate findById(Long id) {
        Preconditions.checkNotNull(id, ERROR_MESSAGE_ID_INVALID + id);
        Preconditions.checkArgument(id >= MIN_ID_VALUE, ERROR_MESSAGE_ID_INVALID + id);

        Specification<Certificate> specification = new FindNotDeletedByIdSpecification(id);
        Optional<Certificate> certificate = certificateRepository.findSingle(specification);
        return certificate
                .orElseThrow(() -> new EntityNotFoundException(ERROR_MESSAGE_CERTIFICATE_NOT_FOUND + id));
    }

    @Override
    public Page<Certificate> findPage(Pageable pageable, Specification<Certificate> specification) {
        Preconditions.checkNotNull(pageable, "Pageable argument invalid: " + pageable);
        Preconditions.checkNotNull(specification, "Specification argument invalid: " + specification);

        return certificateRepository.find(pageable, specification);
    }

    @Override
    @Transactional
    public Certificate create(Certificate certificate) {
        Preconditions.checkNotNull(certificate, ERROR_MESSAGE_CERTIFICATE_INVALID + certificate);

        if (!certificateValidator.isValid(certificate)) {
            throw new ServiceException(ERROR_MESSAGE_CERTIFICATE_INVALID + certificate);
        }

        Set<Tag> tags = certificate.getTags();
        if (tags.isEmpty()) {
            return certificateRepository.save(certificate);
        }

        Set<Tag> consistentTags = tagService.createIfNotExist(tags);
        Certificate certificateWithTags = Certificate.Builder
                .from(certificate)
                .setTags(consistentTags)
                .build();
        return certificateRepository.save(certificateWithTags);
    }

    @Override
    @Transactional
    public Certificate selectiveUpdate(Certificate source) {
        Preconditions.checkNotNull(source, ERROR_MESSAGE_CERTIFICATE_INVALID + source);
        Long id = source.getId();
        if (id == null || id < MIN_ID_VALUE) {
            throw new ServiceException("Unable to update certificate! Id is not specified or invalid: " + id);
        }
        Specification<Certificate> specification = new FindNotDeletedByIdSpecification(id);
        Certificate target = certificateRepository
                .findSingle(specification)
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

    @Override
    public Certificate update(Certificate certificate) {
        Preconditions.checkNotNull(certificate, ERROR_MESSAGE_CERTIFICATE_INVALID + certificate);
        if (!certificateValidator.isValid(certificate)) {
            throw new ServiceException(ERROR_MESSAGE_CERTIFICATE_INVALID + certificate);
        }
        Long id = certificate.getId();
        if (id == null || id < MIN_ID_VALUE) {
            throw new ServiceException("Unable to update certificate! Id is not specified or invalid: " + id);
        }

        Set<Tag> tags = certificate.getTags();
        Set<Tag> consistentTags = tagService.createIfNotExist(tags);
        Certificate updatedWithTags = Certificate.Builder
                .from(certificate)
                .setTags(consistentTags)
                .build();
        return certificateRepository.save(updatedWithTags);
    }

    @Override
    @Transactional
    public Certificate deleteById(Long id) {
        Preconditions.checkNotNull(id, ERROR_MESSAGE_ID_INVALID + id);
        Preconditions.checkArgument(id >= MIN_ID_VALUE, ERROR_MESSAGE_ID_INVALID + id);

        Specification<Certificate> specification = new FindNotDeletedByIdSpecification(id);
        Certificate target = certificateRepository
                .findSingle(specification)
                .orElseThrow(() -> new EntityNotFoundException(ERROR_MESSAGE_CERTIFICATE_NOT_FOUND + id));
        Certificate deleted = Certificate.Builder
                .from(target)
                .setDeleted(true)
                .build();
        return certificateRepository.save(deleted);
    }

}
