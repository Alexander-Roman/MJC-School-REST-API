package com.epam.esm.web.init;

import com.epam.esm.persistence.config.PersistenceConfig;
import com.epam.esm.service.config.ServiceConfig;
import com.epam.esm.web.config.WebConfig;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;
import javax.servlet.ServletContext;
import java.io.IOException;
import java.util.Properties;

public class WebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    private static final String APPLICATION_PROPS = "property/application.properties";

    private final Properties properties;

    public WebInitializer() throws IOException {
        Resource resource = new ClassPathResource(APPLICATION_PROPS);
        properties = PropertiesLoaderUtils.loadProperties(resource);
    }

    @Override
    protected void registerDispatcherServlet(ServletContext servletContext) {
        String profile = (String) properties.get("spring.profiles.active");
        servletContext.setInitParameter("spring.profiles.active", profile);
        super.registerDispatcherServlet(servletContext);
    }

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[]{
                PersistenceConfig.class, ServiceConfig.class
        };
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[]{
                WebConfig.class
        };
    }

    @Override
    @NonNull
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    @Override
    protected Filter[] getServletFilters() {
        HiddenHttpMethodFilter hiddenHttpMethodFilter = new HiddenHttpMethodFilter();
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);
        return new Filter[]{hiddenHttpMethodFilter, characterEncodingFilter};
    }

}
