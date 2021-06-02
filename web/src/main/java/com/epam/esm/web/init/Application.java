package com.epam.esm.web.init;

import com.epam.esm.persistence.config.PersistenceConfig;
import com.epam.esm.security.config.KeycloakSecurityConfig;
import com.epam.esm.service.config.ServiceConfig;
import com.epam.esm.web.config.WebConfig;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application extends SpringBootServletInitializer {

    private static final Class<?>[] PRIMARY_SOURCES = new Class<?>[]{
            Application.class,
            WebConfig.class,
            PersistenceConfig.class,
            ServiceConfig.class,
            KeycloakSecurityConfig.class
    };

    @Bean("jasyptStringEncryptor")
    public StringEncryptor stringEncryptor(@Value("${esmPassword}") String password) {
        StandardPBEStringEncryptor standardPBEStringEncryptor = new StandardPBEStringEncryptor();
        standardPBEStringEncryptor.setPassword(password);
        return standardPBEStringEncryptor;
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(PRIMARY_SOURCES);
    }

    public static void main(String[] args) {
        SpringApplication.run(PRIMARY_SOURCES, args);
    }

}
