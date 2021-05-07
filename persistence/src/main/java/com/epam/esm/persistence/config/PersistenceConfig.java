package com.epam.esm.persistence.config;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.epam.esm.persistence")
@EntityScan(basePackages = {
        "com.epam.esm.persistence.entity",
        "com.epam.esm.persistence.audit.entity"})
public class PersistenceConfig {

    private final String password;

    @Autowired
    public PersistenceConfig(
            @Value("${password}") String password) {
        this.password = password;
    }

    @Bean("jasyptStringEncryptor")
    public StringEncryptor stringEncryptor() {
        StandardPBEStringEncryptor standardPBEStringEncryptor = new StandardPBEStringEncryptor();
        standardPBEStringEncryptor.setPassword(password);
        return standardPBEStringEncryptor;
    }

}
