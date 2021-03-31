package com.epam.esm.web.converter;

import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.web.model.CertificateDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CertificateDtoToEntityConverter implements Converter<CertificateDto, Certificate> {

    @Override
    public Certificate convert(CertificateDto certificateDto) {

        Long id = certificateDto.getId();

        String dtoName = certificateDto.getName();
        String name = dtoName.trim();

        String dtoDescription = certificateDto.getDescription();
        String description = dtoDescription.trim();

        BigDecimal price = certificateDto.getPrice();
        Integer duration = certificateDto.getDuration();
        LocalDateTime createDate = certificateDto.getCreateDate();
        LocalDateTime lastUpdateDate = certificateDto.getLastUpdateDate();

        Set<String> dtoTags = certificateDto.getTags();
        Set<Tag> tags = dtoTags == null
                ? null
                : dtoTags
                .stream()
                .map(String::trim)
                .map(String::toLowerCase)
                .map(tagName -> new Tag(null, tagName))
                .collect(Collectors.toSet());

        return new Certificate(
                id,
                name,
                description,
                price,
                duration,
                createDate,
                lastUpdateDate,
                tags
        );
    }

}
