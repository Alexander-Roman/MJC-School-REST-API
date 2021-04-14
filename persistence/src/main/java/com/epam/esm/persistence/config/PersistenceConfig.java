package com.epam.esm.persistence.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
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

@Configuration
@ComponentScan(basePackages = "com.epam.esm.persistence")
@EnableTransactionManagement
public class PersistenceConfig {

    private final String hikariPropertyFileName;

    public PersistenceConfig(@Value("/property/hikari-${spring.profiles.active:default}.properties") String hikariPropertyFileName) {
        this.hikariPropertyFileName = hikariPropertyFileName;
    }

    @Bean(destroyMethod = "close")
    public HikariDataSource dataSource() {
        HikariConfig hikariConfig = new HikariConfig(hikariPropertyFileName);
        return new HikariDataSource(hikariConfig);
    }

    @Bean
    public NamedParameterJdbcOperations namedParameterJdbcOperations() {
        DataSource dataSource = dataSource();
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        DataSource dataSource = dataSource();
        return new DataSourceTransactionManager(dataSource);
    }

}
