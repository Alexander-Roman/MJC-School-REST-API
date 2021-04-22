package com.epam.esm.web.mapper;

import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.web.model.CertificateDto;
import org.mapstruct.DecoratedWith;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = TagMapper.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
@DecoratedWith(CertificateMapperDecorator.class)
public interface CertificateMapper {

    Certificate map(CertificateDto certificateDto);

    CertificateDto map(Certificate certificate);

}
