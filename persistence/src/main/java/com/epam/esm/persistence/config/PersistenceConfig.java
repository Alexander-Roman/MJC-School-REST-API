package com.epam.esm.persistence.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.Properties;

@Configuration
@ComponentScan(basePackages = "com.epam.esm.persistence")
@EnableTransactionManagement
public class PersistenceConfig {

    private static final String DEFAULT_HIKARI_PROPS = "property/hikari.properties";
    private static final String PROFILE_HIKARI_PROPS = "property/hikari-%s.properties";

    private final Environment environment;

    @Autowired
    public PersistenceConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean(destroyMethod = "close")
    public HikariDataSource dataSource() throws IOException {
        String[] profiles = environment.getActiveProfiles();
        Optional<String> activeProfile = Arrays.stream(profiles).findFirst();
        String path = DEFAULT_HIKARI_PROPS;
        if (activeProfile.isPresent()) {
            String name = activeProfile.get();
            path = String.format(PROFILE_HIKARI_PROPS, name);
        }
        Resource resource = new ClassPathResource(path);
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
