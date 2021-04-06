package com.epam.esm.web.converter;

import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.web.model.CertificateDto;
import org.modelmapper.AbstractConverter;
import org.springframework.core.convert.converter.Converter;

import java.util.Set;
import java.util.stream.Collectors;

public class CertificateEntityToDtoConverter extends AbstractConverter<Certificate, CertificateDto> implements Converter<Certificate, CertificateDto> {

    @Override
    public CertificateDto convert(Certificate certificate) {
        Set<Tag> entityTags = certificate.getTags();
        Set<String> tags = entityTags == null
                ? null
                : entityTags
                .stream()
                .map(Tag::getName)
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
