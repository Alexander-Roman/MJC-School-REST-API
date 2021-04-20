package com.epam.esm.persistence.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;

@Configuration
@Profile("integrationTest")
@ComponentScan(
        basePackages = "com.epam.esm.persistence",
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ANNOTATION,
                value = Configuration.class
        )
)
@EntityScan(basePackages = "com.epam.esm.persistence.entity")
@EnableTransactionManagement
@EnableAutoConfiguration
public class TestPersistenceConfig {

    private static final String SQL_H2_DDL = "classpath:sql.h2/ddl.sql";
    private static final String SQL_H2_DML = "classpath:sql.h2/dml.sql";

    @Bean
    public DataSource dataSource() {
        EmbeddedDatabaseBuilder databaseBuilder = new EmbeddedDatabaseBuilder();
        String scriptEncoding = StandardCharsets.UTF_8.name();
        return databaseBuilder
                .generateUniqueName(true)
                .setType(EmbeddedDatabaseType.H2)
                .setScriptEncoding(scriptEncoding)
                .ignoreFailedDrops(true)
                .addScript(SQL_H2_DDL)
                .addScript(SQL_H2_DML)
                .build();
    }

}
