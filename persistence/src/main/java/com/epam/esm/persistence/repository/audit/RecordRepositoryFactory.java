package com.epam.esm.persistence.repository.audit;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class RecordRepositoryFactory implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        RecordRepositoryFactory.applicationContext = applicationContext;
    }

    public static RecordRepository getRecordRepository() {
        return applicationContext.getBean(RecordRepository.class);
    }

}
