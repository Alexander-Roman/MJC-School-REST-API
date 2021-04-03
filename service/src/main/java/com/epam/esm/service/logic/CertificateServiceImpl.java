package com.epam.esm.service.logic;

import com.epam.esm.persistence.dao.CertificateRepository;
import com.epam.esm.persistence.dao.CertificateTagRepository;
import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.persistence.model.SortRequest;
import com.epam.esm.persistence.specification.Specification;
import com.epam.esm.persistence.specification.certificate.IdSpecification;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.model.FilterRequest;
import com.epam.esm.service.specification.CertificateSpecificationFactory;
import com.epam.esm.service.validator.SortRequestValidator;
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

    private final CertificateRepository certificateRepository;
    private final CertificateTagRepository certificateTagRepository;
    private final Validator<Certificate> certificateValidator;
    private final SortRequestValidator<Certificate> certificateSortRequestValidator;
    private final TagService tagService;

    @Autowired
    public CertificateServiceImpl(CertificateRepository certificateRepository,
                                  CertificateTagRepository certificateTagRepository,
                                  Validator<Certificate> certificateValidator,
                                  SortRequestValidator<Certificate> certificateSortRequestValidator,
                                  TagService tagService) {
        this.certificateRepository = certificateRepository;
        this.certificateTagRepository = certificateTagRepository;
        this.certificateValidator = certificateValidator;
        this.certificateSortRequestValidator = certificateSortRequestValidator;
        this.tagService = tagService;
    }

    @Override
    public Optional<Certificate> findById(Long id) {
        Preconditions.checkArgument(id != null && id >= MIN_ID_VALUE, "Invalid ID parameter: " + id);

        Specification<Certificate> idSpecification = new IdSpecification(id);
        List<Certificate> results = certificateRepository.find(idSpecification);
        return results
                .stream()
                .findFirst();
    }

    @Override
    public List<Certificate> findAll(SortRequest sortRequest, FilterRequest filterRequest) {
        Preconditions.checkNotNull(filterRequest, "Invalid FilterRequest parameter: " + filterRequest);
        if (!certificateSortRequestValidator.isValid(sortRequest)) {
            throw new ServiceException("Invalid SortRequest parameter: " + sortRequest);
        }

        Specification<Certificate> specification = CertificateSpecificationFactory.getByFilterRequest(filterRequest);
        return certificateRepository.findSorted(sortRequest, specification);
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

        Set<Tag> tags = certificate.getTags();
        if (!tags.isEmpty()) {
            tagService.createIfNotExist(tags);
        }

        Certificate saved = certificateRepository.create(dated);

        if (!tags.isEmpty()) {
            certificateTagRepository.resolveAddedTags(saved);
        }

        return saved;
    }

    @Override
    @Transactional
    public Certificate update(Certificate certificate) {
        if (!certificateValidator.isValid(certificate)) {
            throw new ServiceException("Certificate invalid: " + certificate);
        }
        Long id = certificate.getId();
        if (id == null || id < MIN_ID_VALUE) {
            throw new ServiceException("Unable to update certificate! Id not specified!");
        }
        Set<Tag> tags = certificate.getTags();
        if (!tags.isEmpty()) {
            tagService.createIfNotExist(tags);
        }
        LocalDateTime now = LocalDateTime.now();
        Certificate dated = Certificate.Builder
                .from(certificate)
                .setLastUpdateDate(now)
                .build();
        Certificate updated = certificateRepository.update(dated);
        if (!tags.isEmpty()) {
            certificateTagRepository.resolveAddedTags(updated);
        }
        certificateTagRepository.resolveRemovedTags(updated);
        tagService.deleteUnused();
        return updated;
    }

    @Override
    public Certificate selectiveUpdate(Certificate source, Certificate target) {
        Preconditions.checkNotNull(source, "Invalid source Certificate: " + source);
        Preconditions.checkNotNull(source, "Invalid target Certificate: " + target);
        Long sourceId = source.getId();
        Long targetId = target.getId();
        Preconditions.checkNotNull(sourceId, "Invalid source Certificate id: " + source);
        Preconditions.checkNotNull(targetId, "Invalid target Certificate id: " + target);
        if (!sourceId.equals(targetId)) {
            throw new ServiceException("Updating certificates with different ids is not acceptable!");
        }
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
    @Transactional
    public void deleteById(Long id) {
        Preconditions.checkArgument(id != null && id >= MIN_ID_VALUE, "Invalid ID parameter: " + id);
        certificateTagRepository.deleteByCertificateId(id);
        certificateRepository.delete(id);
        tagService.deleteUnused();
    }

}
