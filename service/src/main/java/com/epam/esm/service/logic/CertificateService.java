package com.epam.esm.service.logic;

import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.persistence.model.FilterRequest;
import com.epam.esm.persistence.model.SortRequest;

import java.util.List;

public interface CertificateService {

    Certificate findById(Long id);

    List<Certificate> findAll(SortRequest sortRequest, FilterRequest filterRequest);

    Certificate create(Certificate certificate);

    Certificate selectiveUpdate(Certificate certificate);

    Certificate deleteById(Long id);

}
