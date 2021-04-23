package com.epam.esm.web.assember;

import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.web.controller.CertificateController;
import com.epam.esm.web.mapper.CertificateMapper;
import com.epam.esm.web.model.CertificateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CertificateDtoAssemblerImpl extends RepresentationModelAssemblerSupport<Certificate, CertificateDto> implements CertificateDtoAssembler {

    private final CertificateMapper certificateMapper;

    @Autowired
    public CertificateDtoAssemblerImpl(CertificateMapper certificateMapper) {
        super(CertificateController.class, CertificateDto.class);
        this.certificateMapper = certificateMapper;
    }

    @Override
    public CertificateDto toModel(Certificate entity) {
        CertificateDto certificateDto = certificateMapper.map(entity);
        Long id = certificateDto.getId();
        return certificateDto.add(linkTo(methodOn(CertificateController.class).getCertificateById(id)).withSelfRel());
    }

}
