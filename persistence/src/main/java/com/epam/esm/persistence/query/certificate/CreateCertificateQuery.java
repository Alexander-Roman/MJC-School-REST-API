package com.epam.esm.persistence.query.certificate;

import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.persistence.query.UpdateQuery;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CreateCertificateQuery implements UpdateQuery<Certificate> {

    private static final String SQL_INSERT_NEW = "\n" +
            "INSERT INTO certificate (name, description, price, duration, create_date, last_update_date) \n" +
            "VALUES (:name, :description, :price, :duration, :createDate, :lastUpdateDate) \n";
    private final Certificate certificate;

    public CreateCertificateQuery(Certificate certificate) {
        this.certificate = certificate;
    }

    @Override
    public String getSql() {
        return SQL_INSERT_NEW;
    }

    @Override
    public Map<String, Object> getParameters() {
        Map<String, Object> namedParameters = new HashMap<>();
        String name = certificate.getName();
        String description = certificate.getDescription();
        BigDecimal price = certificate.getPrice();
        Integer duration = certificate.getDuration();
        LocalDateTime createDate = certificate.getCreateDate();
        LocalDateTime lastUpdateDate = certificate.getLastUpdateDate();
        namedParameters.put("name", name);
        namedParameters.put("description", description);
        namedParameters.put("price", price);
        namedParameters.put("duration", duration);
        namedParameters.put("createDate", createDate);
        namedParameters.put("lastUpdateDate", lastUpdateDate);
        return Collections.unmodifiableMap(namedParameters);
    }
}
