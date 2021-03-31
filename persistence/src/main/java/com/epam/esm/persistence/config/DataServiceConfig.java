package com.epam.esm.persistence.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
public class DataServiceConfig {

    private static final String HIKARI_PROPS = "property/hikari.properties";

    @Bean(destroyMethod = "close")
    public HikariDataSource dataSource() throws IOException {
        Resource resource = new ClassPathResource(HIKARI_PROPS);
        Properties hikariProperties = PropertiesLoaderUtils.loadProperties(resource);
        HikariConfig hikariConfig = new HikariConfig(hikariProperties);
        return new HikariDataSource(hikariConfig);
    }

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate() throws IOException {
        DataSource dataSource = dataSource();
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean
    public PlatformTransactionManager transactionManager() throws IOException {
        DataSource dataSource = dataSource();
        return new DataSourceTransactionManager(dataSource);
    }

}
