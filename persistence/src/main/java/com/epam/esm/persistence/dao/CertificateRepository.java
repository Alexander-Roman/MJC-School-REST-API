package com.epam.esm.persistence.dao;

import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.persistence.specification.Specification;

import java.util.List;

public interface CertificateRepository {

    List<Certificate> find(Specification<Certificate> specification);

}
