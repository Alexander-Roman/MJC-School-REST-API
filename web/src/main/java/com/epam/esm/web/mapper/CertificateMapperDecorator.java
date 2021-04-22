package com.epam.esm.web.mapper;

import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.web.controller.CertificateController;
import com.epam.esm.web.model.CertificateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public abstract class CertificateMapperDecorator implements CertificateMapper {

    @Autowired
    @Qualifier("delegate")
    private CertificateMapper delegate;

    @Override
    public CertificateDto map(Certificate certificate) {
        CertificateDto certificateDto = delegate.map(certificate);
        Long id = certificateDto.getId();
        certificateDto.add(linkTo(methodOn(CertificateController.class).getCertificateById(id)).withSelfRel());
        return certificateDto;
    }

}
