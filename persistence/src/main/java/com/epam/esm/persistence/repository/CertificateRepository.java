package com.epam.esm.persistence.repository;

import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.persistence.model.SortRequest;
import com.epam.esm.persistence.specification.Specification;

import java.util.List;

public interface CertificateRepository {

    List<Certificate> find(Specification<Certificate> specification);

    List<Certificate> find(SortRequest sortRequest, Specification<Certificate> specification);

    Certificate create(Certificate certificate);

    Certificate update(Certificate certificate);

    void delete(Long id);

}
