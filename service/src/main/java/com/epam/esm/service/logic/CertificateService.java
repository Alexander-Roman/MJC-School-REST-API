package com.epam.esm.service.logic;

import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.persistence.model.SortRequest;
import com.epam.esm.service.model.FilterRequest;

import java.util.List;
import java.util.Optional;

public interface CertificateService {

    Optional<Certificate> findById(Long id);

    List<Certificate> findAll(SortRequest sortRequest, FilterRequest filterRequest);

    Certificate create(Certificate certificate);

    Certificate update(Certificate certificate);

    Certificate selectiveUpdate(Certificate source, Certificate target);

}
