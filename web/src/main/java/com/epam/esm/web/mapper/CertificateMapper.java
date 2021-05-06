package com.epam.esm.web.mapper;

import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.web.model.CertificateDto;

public interface CertificateMapper {

    Certificate map(CertificateDto certificateDto);

    CertificateDto map(Certificate certificate);

    Certificate mapMerge(CertificateDto certificateDto, Certificate certificate);

}
