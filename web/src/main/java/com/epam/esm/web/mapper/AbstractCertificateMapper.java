package com.epam.esm.web.mapper;

import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.web.model.CertificateDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses = TagMapper.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class AbstractCertificateMapper implements CertificateMapper {

    @Override
    public abstract Certificate map(CertificateDto certificateDto);

    @Override
    public abstract CertificateDto map(Certificate certificate);

    @Override
    public Certificate mapMerge(CertificateDto certificateDto, Certificate certificate) {
        Certificate.Builder builder = Certificate.Builder.from(certificate);
        Certificate.Builder merged = this.map(certificateDto, builder);
        return merged.build();
    }

    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createDate", ignore = true)
    @Mapping(target = "lastUpdateDate", ignore = true)
    protected abstract Certificate.Builder map(CertificateDto certificateDto, @MappingTarget Certificate.Builder certificateBuilder);

}
