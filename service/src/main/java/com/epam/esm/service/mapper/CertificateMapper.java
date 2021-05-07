package com.epam.esm.service.mapper;

import com.epam.esm.persistence.entity.Certificate;

public interface CertificateMapper {

    Certificate merge(Certificate source, Certificate target);

}
