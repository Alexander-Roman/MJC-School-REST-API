package com.epam.esm.persistence.repository;

import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.persistence.model.FilterRequest;
import com.epam.esm.persistence.model.SortRequest;

import java.util.List;
import java.util.Optional;

public interface CertificateRepository {

    Optional<Certificate> findById(Long id);

    List<Certificate> findAll(SortRequest sortRequest, FilterRequest filterRequest);

    Certificate save(Certificate certificate);

    void delete(Certificate certificate);

}
