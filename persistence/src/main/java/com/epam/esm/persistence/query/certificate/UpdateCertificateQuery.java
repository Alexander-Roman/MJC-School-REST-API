package com.epam.esm.persistence.query.certificate;

import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.persistence.query.UpdateQuery;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class UpdateCertificateQuery implements UpdateQuery<Certificate> {

    private static final String SQL_UPDATE_EXISTING =
            "        UPDATE certificate \n" +
                    "SET name             = :name, \n" +
                    "    description      = :description, \n" +
                    "    price            = :price, \n" +
                    "    duration         = :duration, \n" +
                    "    create_date      = :createDate, \n" +
                    "    last_update_date = :lastUpdateDate \n" +
                    "WHERE id = :id  \n";

    private final Certificate certificate;

    public UpdateCertificateQuery(Certificate certificate) {
        this.certificate = certificate;
    }

    @Override
    public String getSql() {
        return SQL_UPDATE_EXISTING;
    }

    @Override
    public Map<String, Object> getParameters() {
        Map<String, Object> namedParameters = new HashMap<>();
        Long id = certificate.getId();
        namedParameters.put("id", id);
        String name = certificate.getName();
        namedParameters.put("name", name);
        String description = certificate.getDescription();
        namedParameters.put("description", description);
        BigDecimal price = certificate.getPrice();
        namedParameters.put("price", price);
        Integer duration = certificate.getDuration();
        namedParameters.put("duration", duration);
        LocalDateTime createDate = certificate.getCreateDate();
        namedParameters.put("createDate", createDate);
        LocalDateTime lastUpdateDate = certificate.getLastUpdateDate();
        namedParameters.put("lastUpdateDate", lastUpdateDate);
        return Collections.unmodifiableMap(namedParameters);
    }

}
