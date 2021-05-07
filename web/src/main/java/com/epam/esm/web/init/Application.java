package com.epam.esm.web.init;

import com.epam.esm.persistence.config.PersistenceConfig;
import com.epam.esm.service.config.ServiceConfig;
import com.epam.esm.web.config.WebConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        Class<?>[] primarySources = new Class<?>[]{Application.class, WebConfig.class, PersistenceConfig.class, ServiceConfig.class};
        SpringApplication.run(primarySources, args);
    }
}
