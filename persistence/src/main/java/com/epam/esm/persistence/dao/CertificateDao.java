package com.epam.esm.persistence.dao;

import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.persistence.model.FilterRequest;
import com.epam.esm.persistence.model.SortRequest;

import java.util.List;
import java.util.Optional;

public interface CertificateDao {

    Optional<Certificate> findById(Long id);

    List<Certificate> findAll();

    List<Certificate> findAll(SortRequest sortRequest, FilterRequest filterRequest);

    Certificate create(Certificate certificate);

    Certificate update(Certificate certificate);

    void delete(Long id);

}
