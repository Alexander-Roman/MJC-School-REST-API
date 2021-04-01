package com.epam.esm.service.logic;

import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.service.model.FilterRequest;

import java.util.List;
import java.util.Optional;

public interface CertificateService {

    Optional<Certificate> findById(Long id);

    List<Certificate> findAll(FilterRequest filterRequest);

}
