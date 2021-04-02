package com.epam.esm.service.logic;

import com.epam.esm.persistence.dao.CertificateRepository;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CertificateServiceImpl implements CertificateService {

    public static final long MIN_ID_VALUE = 1L;

    private final CertificateRepository certificateRepository;
    private final Validator<Certificate> certificateValidator;
    private final SortRequestValidator<Certificate> certificateSortRequestValidator;

    @Autowired
    public CertificateServiceImpl(CertificateRepository certificateRepository,
                                  Validator<Certificate> certificateValidator, SortRequestValidator<Certificate> certificateSortRequestValidator) {
        this.certificateRepository = certificateRepository;
        this.certificateValidator = certificateValidator;
        this.certificateSortRequestValidator = certificateSortRequestValidator;
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

//        tagService.createIfNotExist(tags);
//        Certificate saved = certificateDao.save(dated);
//        Long generatedId = saved.getCertificateId();
//        certificateTagService.resolveAddedTags(generatedId, tags);
//        return saved;

        return null;
    }

    @Override
    public Certificate selectiveUpdate(Certificate certificate) {
        return null;
    }
}
