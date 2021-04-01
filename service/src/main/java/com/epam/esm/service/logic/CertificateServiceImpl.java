package com.epam.esm.service.logic;

import com.epam.esm.persistence.dao.CertificateRepository;
import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.persistence.model.SortRequest;
import com.epam.esm.persistence.specification.Specification;
import com.epam.esm.persistence.specification.certificate.IdSpecification;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.model.FilterRequest;
import com.epam.esm.service.specification.CertificateSpecificationFactory;
import com.epam.esm.service.validator.SortRequestValidator;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CertificateServiceImpl implements CertificateService {

    public static final long MIN_ID_VALUE = 1L;

    private final CertificateRepository certificateRepository;
    private final SortRequestValidator<Certificate> certificateSortRequestValidator;

    @Autowired
    public CertificateServiceImpl(CertificateRepository certificateRepository,
                                  SortRequestValidator<Certificate> certificateSortRequestValidator) {
        this.certificateRepository = certificateRepository;
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

}
