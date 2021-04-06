package com.epam.esm.web.validator;

import com.epam.esm.web.model.CertificateDto;
import com.epam.esm.web.model.TagDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Component
public class CertificateDtoCreateValidatorImpl implements CertificateDtoCreateValidator {

    private static final int MAX_NAME_LENGTH = 150;
    private static final int MAX_DESCRIPTION_LENGTH = 255;
    private static final BigDecimal MAX_PRICE = new BigDecimal("99999.99");
    private static final int MIN_DURATION = 1;

    private final TagDtoValidator tagDtoValidator;

    @Autowired
    public CertificateDtoCreateValidatorImpl(TagDtoValidator tagDtoValidator) {
        this.tagDtoValidator = tagDtoValidator;
    }

    @Override
    public void validate(@NonNull Object object, @NonNull Errors errors) {
        CertificateDto certificateDto = (CertificateDto) object;

        Long id = certificateDto.getId();
        if (id != null) {
            errors.rejectValue(CertificateDto.Field.ID, "certificate.dto.id.create", "Specifying certificate id is not allowed for create operations!");
        }

        String name = certificateDto.getName();
        if (name == null) {
            errors.rejectValue(CertificateDto.Field.NAME, "certificate.dto.name.create", "Certificate name required for create operation!");
        } else {
            if (name.length() > MAX_NAME_LENGTH) {
                errors.rejectValue(CertificateDto.Field.NAME, "certificate.dto.name.invalid", "Certificate name length should be not greater than 150 characters!");
            }
            if (name.trim().isEmpty()) {
                errors.rejectValue(CertificateDto.Field.NAME, "certificate.dto.name.blank", "Certificate name length should be not blank!");
            }
        }

        String description = certificateDto.getDescription();
        if (description == null) {
            errors.rejectValue(CertificateDto.Field.DESCRIPTION, "certificate.dto.description.crate", "Certificate description required for create operation!");
        } else {
            if (description.length() > MAX_DESCRIPTION_LENGTH) {
                errors.rejectValue(CertificateDto.Field.DESCRIPTION, "certificate.dto.description.invalid", "Certificate description length should be not greater than 255 characters!");
            }
            if (description.trim().isEmpty()) {
                errors.rejectValue(CertificateDto.Field.DESCRIPTION, "certificate.dto.description.blank", "Certificate description should be not blank!");
            }
        }

        BigDecimal price = certificateDto.getPrice();
        if (price == null) {
            errors.rejectValue(CertificateDto.Field.PRICE, "certificate.dto.price.create", "Certificate price required for create operation!");
        } else {
            if (price.compareTo(BigDecimal.ZERO) < 0) {
                errors.rejectValue(CertificateDto.Field.PRICE, "certificate.dto.price.negative", "Negative price is not allowed!");
            }
            if (price.compareTo(MAX_PRICE) > 0) {
                errors.rejectValue(CertificateDto.Field.PRICE, "certificate.dto.price.overmuch", "Price value more than 99999.99 is not allowed!");
            }
        }

        Integer duration = certificateDto.getDuration();
        if (duration == null) {
            errors.rejectValue(CertificateDto.Field.DURATION, "certificate.dto.duration.create", "Certificate duration required for create operation!");
        } else if (duration < MIN_DURATION) {
            errors.rejectValue(CertificateDto.Field.DURATION, "certificate.dto.duration.invalid", "Duration can not be less than 1 day!");
        }

        LocalDateTime createDate = certificateDto.getCreateDate();
        if (createDate != null) {
            errors.rejectValue(CertificateDto.Field.CREATE_DATE, "certificate.dto.create.date.specified", "Specifying creation date is not allowed!");
        }

        LocalDateTime lastUpdateDate = certificateDto.getLastUpdateDate();
        if (lastUpdateDate != null) {
            errors.rejectValue(CertificateDto.Field.LAST_UPDATE_DATE, "certificate.dto.last.update.date.specified", "Specifying last update date is not allowed!");
        }

        Set<TagDto> tags = certificateDto.getTags();
        if (tags == null) {
            errors.rejectValue(CertificateDto.Field.TAGS, "certificate.dto.tags.create", "Certificate tags required for create operation!");
        } else {
            for (TagDto tag : tags) {
                tagDtoValidator.validate(tag, errors);
            }
        }
    }

}
