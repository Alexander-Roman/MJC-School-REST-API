package com.epam.esm.service.validator;

import com.epam.esm.persistence.entity.CertificateTag;
import org.springframework.stereotype.Component;

@Component
public class CertificateTagValidator implements Validator<CertificateTag> {

    private static final long MIN_ID = 1;

    @Override
    public boolean isValid(CertificateTag certificateTag) {
        if (certificateTag == null) {
            return false;
        }
        Long id = certificateTag.getId();
        if (id != null && id < MIN_ID) {
            return false;
        }
        Long certificateId = certificateTag.getCertificateId();
        if (certificateId == null || certificateId < MIN_ID) {
            return false;
        }
        Long tagId = certificateTag.getTagId();
        return tagId != null && tagId >= MIN_ID;
    }

}
