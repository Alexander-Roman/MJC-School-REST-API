package com.epam.esm.service.logic;

import com.epam.esm.persistence.entity.Certificate;

import java.util.Optional;

public interface CertificateService {

    Optional<Certificate> findById(Long id);

}
