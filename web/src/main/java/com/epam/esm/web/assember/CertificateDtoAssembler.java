package com.epam.esm.web.assember;

import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.web.model.CertificateDto;
import org.springframework.hateoas.server.RepresentationModelAssembler;

/**
 * Interface for components that convert Certificate type into CertificateDto as RepresentationModel
 */
public interface CertificateDtoAssembler extends RepresentationModelAssembler<Certificate, CertificateDto> {

}
