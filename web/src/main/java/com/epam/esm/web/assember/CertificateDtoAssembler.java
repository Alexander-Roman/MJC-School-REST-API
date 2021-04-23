package com.epam.esm.web.assember;

import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.web.model.CertificateDto;
import org.springframework.hateoas.server.RepresentationModelAssembler;

public interface CertificateDtoAssembler extends RepresentationModelAssembler<Certificate, CertificateDto> {

}
