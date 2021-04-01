package com.epam.esm.web.validator;

import com.epam.esm.web.model.CertificateDto;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.math.BigDecimal;
import java.util.Set;

@Component
public class CertificateDtoValidator implements Validator {

    private static final long MIN_ID = 1;
    private static final int MAX_NAME_LENGTH = 150;
    private static final int MAX_DESCRIPTION_LENGTH = 255;
    private static final BigDecimal MAX_PRICE = new BigDecimal("99999.99");
    private static final int MIN_DURATION = 1;
    private static final int MAX_TAG_NAME_LENGTH = 50;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return CertificateDto.class.equals(clazz);
    }

    @Override
    public void validate(@NonNull Object object, @NonNull Errors errors) {
        CertificateDto certificateDto = (CertificateDto) object;

        Long id = certificateDto.getId();
        if (id != null && id < MIN_ID) {
            errors.rejectValue(CertificateDto.Field.ID, "certificate.dto.id.invalid", "Id should be not less than 1!");
        }

        String name = certificateDto.getName();
        if (name != null) {
            if (name.length() > MAX_NAME_LENGTH) {
                errors.rejectValue(CertificateDto.Field.NAME, "certificate.dto.name.invalid", "Certificate name length should be not greater than 150 characters!");
            }
            if (name.trim().isEmpty()) {
                errors.rejectValue(CertificateDto.Field.NAME, "certificate.dto.name.blank", "Certificate name length should be not blank!");
            }
        }

        String description = certificateDto.getDescription();
        if (description != null) {
            if (description.length() > MAX_DESCRIPTION_LENGTH) {
                errors.rejectValue(CertificateDto.Field.DESCRIPTION, "certificate.dto.description.invalid", "Certificate description length should be not greater than 255 characters!");
            }
            if (description.trim().isEmpty()) {
                errors.rejectValue(CertificateDto.Field.DESCRIPTION, "certificate.dto.description.blank", "Certificate description should be not blank!");
            }
        }

        BigDecimal price = certificateDto.getPrice();
        if (price != null) {
            if (price.compareTo(BigDecimal.ZERO) < 0) {
                errors.rejectValue(CertificateDto.Field.PRICE, "certificate.dto.price.negative", "Negative price is not allowed!");
            }
            if (price.compareTo(MAX_PRICE) > 0) {
                errors.rejectValue(CertificateDto.Field.PRICE, "certificate.dto.price.overmuch", "Price value more than 99999.99 is not allowed!");
            }
        }

        Integer duration = certificateDto.getDuration();
        if (duration != null && duration < MIN_DURATION) {
            errors.rejectValue(CertificateDto.Field.DURATION, "certificate.dto.duration.invalid", "Duration can not be less than 1 day!");
        }

        Set<String> tags = certificateDto.getTags();
        if (tags != null) {
            for (String tag : tags) {
                if (tag.length() > MAX_TAG_NAME_LENGTH) {
                    errors.rejectValue(CertificateDto.Field.TAGS, "certificate.dto.tags.invalid", new Object[]{tag}, "Tag name should be not greater than 50 characters: " + tag);
                }
                String trimmed = tag.trim();
                if ("".equals(trimmed)) {
                    errors.rejectValue(CertificateDto.Field.TAGS, "certificate.dto.tags.empty", new Object[]{tag}, "Blank tag is not allowed: " + tag);
                }
            }
        }
    }

}
