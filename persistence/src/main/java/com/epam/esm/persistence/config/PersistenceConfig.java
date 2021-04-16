package com.epam.esm.persistence.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.properties.EncryptableProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Configuration
@ComponentScan(basePackages = "com.epam.esm.persistence")
@EnableTransactionManagement
public class PersistenceConfig {

    private final String hikariPropertyFileName;

    public PersistenceConfig(@Value("/property/hikari-${spring.profiles.active:default}.properties") String hikariPropertyFileName) {
        this.hikariPropertyFileName = hikariPropertyFileName;
    }

    @Bean
    public StringEncryptor stringEncryptor() {
        StandardPBEStringEncryptor standardPBEStringEncryptor = new StandardPBEStringEncryptor();
        standardPBEStringEncryptor.setPassword("password");
        return standardPBEStringEncryptor;
    }

    @Bean(destroyMethod = "close")
    public HikariDataSource dataSource() throws IOException {
        StringEncryptor stringEncryptor = stringEncryptor();
        Properties hikariProperties = new EncryptableProperties(stringEncryptor);
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(hikariPropertyFileName);
        hikariProperties.load(inputStream);

        hikariProperties.entrySet().forEach(entry -> {
            String key = entry.getKey().toString();
            Object value = hikariProperties.getProperty(key);
            entry.setValue(value);
        });

        HikariConfig hikariConfig = new HikariConfig(hikariProperties);
        return new HikariDataSource(hikariConfig);
    }

    @Bean
    public NamedParameterJdbcOperations namedParameterJdbcOperations() throws IOException {
        DataSource dataSource = dataSource();
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean
    public PlatformTransactionManager transactionManager() throws IOException {
        DataSource dataSource = dataSource();
        return new DataSourceTransactionManager(dataSource);
    }

}
