package com.epam.esm.persistence.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan(basePackages = "com.epam.esm.persistence")
@EntityScan(basePackages = {
        "com.epam.esm.persistence.entity",
        "com.epam.esm.persistence.audit.entity"})
@EnableJpaRepositories("com.epam.esm.persistence.repository")
public class PersistenceConfig {

}
