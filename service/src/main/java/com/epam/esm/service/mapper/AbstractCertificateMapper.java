package com.epam.esm.service.mapper;

import com.epam.esm.persistence.entity.Certificate;
import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class AbstractCertificateMapper implements CertificateMapper {

    @Override
    public Certificate merge(Certificate source, Certificate target) {
        Certificate.Builder builder = Certificate.Builder.from(target);
        Certificate.Builder merged = this.map(source, builder);
        return merged.build();
    }

    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createDate", ignore = true)
    @Mapping(target = "lastUpdateDate", ignore = true)
    protected abstract Certificate.Builder map(Certificate source, @MappingTarget Certificate.Builder targetBuilder);

}
