package com.epam.esm.service.validator;

import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.persistence.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Component
public class CertificateValidator implements Validator<Certificate> {

    private static final long MIN_ID = 1;
    private static final int MAX_NAME_LENGTH = 150;
    private static final int MAX_DESCRIPTION_LENGTH = 255;
    private static final BigDecimal MAX_PRICE = new BigDecimal("99999.99");
    private static final int MIN_DURATION = 1;

    private final Validator<Tag> tagValidator;

    @Autowired
    public CertificateValidator(Validator<Tag> tagValidator) {
        this.tagValidator = tagValidator;
    }

    @Override
    public boolean isValid(Certificate certificate) {
        if (certificate == null) {
            return false;
        }

        Long id = certificate.getId();
        if (id != null && id < MIN_ID) {
            return false;
        }

        String name = certificate.getName();
        if (name == null || name.length() > MAX_NAME_LENGTH || name.trim().isEmpty()) {
            return false;
        }

        String description = certificate.getDescription();
        if (description == null || description.length() > MAX_DESCRIPTION_LENGTH || description.trim().isEmpty()) {
            return false;
        }

        BigDecimal price = certificate.getPrice();
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0 || price.compareTo(MAX_PRICE) > 0) {
            return false;
        }

        Integer duration = certificate.getDuration();
        if (duration == null || duration < MIN_DURATION) {
            return false;
        }

        LocalDateTime createDate = certificate.getCreateDate();
        if (createDate == null) {
            return false;
        }

        LocalDateTime lastUpdateDate = certificate.getLastUpdateDate();
        if (lastUpdateDate == null) {
            return false;
        }

        Set<Tag> tags = certificate.getTags();
        if (tags == null) {
            return false;
        }

        for (Tag tag : tags) {
            if (!tagValidator.isValid(tag)) {
                return false;
            }
        }

        Boolean deleted = certificate.getDeleted();
        return deleted != null;
    }

}
