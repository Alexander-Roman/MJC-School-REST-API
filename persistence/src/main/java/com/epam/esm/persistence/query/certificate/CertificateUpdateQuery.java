package com.epam.esm.persistence.query.certificate;

import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.persistence.query.UpdateQuery;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CertificateUpdateQuery implements UpdateQuery<Certificate> {

    private static final String SQL_UPDATE_EXISTING = "\n" +
            "UPDATE certificate \n" +
            "SET name             = :name, \n" +
            "    description      = :description, \n" +
            "    price            = :price, \n" +
            "    duration         = :duration, \n" +
            "    create_date      = :createDate, \n" +
            "    last_update_date = :lastUpdateDate \n" +
            "WHERE id = :id  \n";

    private final Certificate certificate;

    public CertificateUpdateQuery(Certificate certificate) {
        this.certificate = certificate;
    }

    @Override
    public String getSql() {
        return SQL_UPDATE_EXISTING;
    }

    @Override
    public Map<String, Object> getParameters() {
        Long id = certificate.getId();
        String name = certificate.getName();
        String description = certificate.getDescription();
        BigDecimal price = certificate.getPrice();
        Integer duration = certificate.getDuration();
        LocalDateTime createDate = certificate.getCreateDate();
        LocalDateTime lastUpdateDate = certificate.getLastUpdateDate();

        Map<String, Object> namedParameters = new HashMap<>();
        namedParameters.put("id", id);
        namedParameters.put("name", name);
        namedParameters.put("description", description);
        namedParameters.put("price", price);
        namedParameters.put("duration", duration);
        namedParameters.put("createDate", createDate);
        namedParameters.put("lastUpdateDate", lastUpdateDate);
        return Collections.unmodifiableMap(namedParameters);
    }

}
