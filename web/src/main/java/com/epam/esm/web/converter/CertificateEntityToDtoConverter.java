package com.epam.esm.web.converter;

import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.web.model.CertificateDto;
import com.epam.esm.web.model.TagDto;
import org.modelmapper.AbstractConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CertificateEntityToDtoConverter extends AbstractConverter<Certificate, CertificateDto> implements Converter<Certificate, CertificateDto> {

    private final Converter<Tag, TagDto> tagEntityToDtoConverter;

    @Autowired
    public CertificateEntityToDtoConverter(Converter<Tag, TagDto> tagEntityToDtoConverter) {
        this.tagEntityToDtoConverter = tagEntityToDtoConverter;
    }

    @Override
    public CertificateDto convert(Certificate certificate) {
        Set<Tag> entityTags = certificate.getTags();
        Set<TagDto> tags = entityTags == null
                ? null
                : entityTags
                .stream()
                .map(tagEntityToDtoConverter::convert)
                .collect(Collectors.toSet());
        return new CertificateDto(
                certificate.getId(),
                certificate.getName(),
                certificate.getDescription(),
                certificate.getPrice(),
                certificate.getDuration(),
                certificate.getCreateDate(),
                certificate.getLastUpdateDate(),
                tags
        );
    }

}
