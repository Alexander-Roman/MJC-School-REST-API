package com.epam.esm.persistence.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.epam.esm.persistence")
@EntityScan(basePackages = {
        "com.epam.esm.persistence.entity",
        "com.epam.esm.persistence.audit.entity"})
public class PersistenceConfig {

}
