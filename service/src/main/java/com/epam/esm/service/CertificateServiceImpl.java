package com.epam.esm.service;

import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.persistence.repository.CertificateRepository;
import com.epam.esm.persistence.specification.certificare.FindNotDeletedByIdSpecification;
import com.epam.esm.service.exception.EntityNotFoundException;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.mapper.CertificateMapper;
import com.epam.esm.service.validator.Validator;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final CertificateMapper certificateMapper;

    @Autowired
    public CertificateServiceImpl(CertificateRepository certificateRepository,
                                  TagService tagService,
                                  Validator<Certificate> certificateValidator,
                                  CertificateMapper certificateMapper) {
        this.certificateRepository = certificateRepository;
        this.tagService = tagService;
        this.certificateValidator = certificateValidator;
        this.certificateMapper = certificateMapper;
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

        Certificate certificate = certificateMapper.merge(source, target);
        return this.update(certificate);
    }

    private Certificate update(Certificate certificate) {
        if (!certificateValidator.isValid(certificate)) {
            throw new ServiceException(ERROR_MESSAGE_CERTIFICATE_INVALID + certificate);
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
