package com.epam.esm.service.logic;

import com.epam.esm.persistence.dao.CertificateRepository;
import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.persistence.specification.Specification;
import com.epam.esm.persistence.specification.certificate.CertificateIdSpecification;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CertificateServiceImpl implements CertificateService {

    public static final long MIN_ID_VALUE = 1L;

    private final CertificateRepository certificateRepository;

    @Autowired
    public CertificateServiceImpl(CertificateRepository certificateRepository) {
        this.certificateRepository = certificateRepository;
    }

    @Override
    public Optional<Certificate> findById(Long id) {
        Preconditions.checkArgument(id != null && id >= MIN_ID_VALUE, "Invalid ID parameter: " + id);
        Specification<Certificate> idSpecification = new CertificateIdSpecification(id);
        List<Certificate> results = certificateRepository.find(idSpecification);
        return results
                .stream()
                .findFirst();
    }

}
