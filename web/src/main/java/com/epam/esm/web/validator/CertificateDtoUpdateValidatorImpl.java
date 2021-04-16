package com.epam.esm.web.validator;

import com.epam.esm.web.model.CertificateDto;
import com.epam.esm.web.model.TagDto;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.math.BigDecimal;
import java.util.Set;

@Component
public class CertificateDtoUpdateValidatorImpl implements CertificateDtoUpdateValidator {

    private static final long MIN_ID = 1;
    private static final int MAX_NAME_LENGTH = 150;
    private static final int MAX_DESCRIPTION_LENGTH = 255;
    private static final BigDecimal MAX_PRICE = new BigDecimal("99999.99");
    private static final int MIN_DURATION = 1;
    private static final int MAX_TAG_NAME_LENGTH = 50;

    @Override
    public void validate(@NonNull Object object,
                         @NonNull Errors errors) {
        CertificateDto certificateDto = (CertificateDto) object;

        Long id = certificateDto.getId();
        if (id == null) {
            errors.rejectValue(CertificateDto.Field.ID, "certificate.dto.id.update", "Id should be specified for update operations!");
        } else if (id < MIN_ID) {
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

        Set<TagDto> tags = certificateDto.getTags();
        if (tags != null) {
            this.validate(tags, errors);
        }
    }

    private void validate(Set<TagDto> tags, Errors errors) {
        for (TagDto tagDto : tags) {
            if (tagDto == null) {
                errors.rejectValue(CertificateDto.Field.TAGS, "certificate.dto.tags.null", "Certificate tags should not contain NULL values!");
            } else {
                String name = tagDto.getName();
                if (name == null) {
                    errors.rejectValue(CertificateDto.Field.TAGS, "certificate.dto.tags.name.null", "Certificate tags should not contain tags without name!");
                } else {
                    if (name.length() > MAX_TAG_NAME_LENGTH) {
                        errors.rejectValue(CertificateDto.Field.TAGS, "certificate.dto.tags.name.invalid", "Certificate tags should not contain tags with name greater than 50 characters!");
                    }
                    if (name.trim().isEmpty()) {
                        errors.rejectValue(CertificateDto.Field.TAGS, "certificate.dto.tags.name.blank", "Certificate tags should not contain tags with blank name!");
                    }
                }
            }
        }
    }

}
