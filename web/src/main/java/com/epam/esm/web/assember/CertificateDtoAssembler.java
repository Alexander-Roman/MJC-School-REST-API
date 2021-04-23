package com.epam.esm.web.assember;

import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.web.controller.CertificateController;
import com.epam.esm.web.mapper.CertificateMapper;
import com.epam.esm.web.model.CertificateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class CertificateDtoAssembler extends RepresentationModelAssemblerSupport<Certificate, CertificateDto> {

    private final CertificateMapper certificateMapper;

    @Autowired
    public CertificateDtoAssembler(CertificateMapper certificateMapper) {
        super(CertificateController.class, CertificateDto.class);
        this.certificateMapper = certificateMapper;
    }

    @Override
    public CertificateDto toModel(Certificate entity) {
        return certificateMapper.map(entity);
    }

    @Override
    public CollectionModel<CertificateDto> toCollectionModel(Iterable<? extends Certificate> entities) {
        return super.toCollectionModel(entities);
    }
}
