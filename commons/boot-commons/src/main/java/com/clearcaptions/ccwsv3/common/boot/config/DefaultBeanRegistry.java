package com.clearcaptions.ccwsv3.common.boot.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import com.clearcaptions.ccwsv3.common.boot.config.pagination.PaginatedResourceAssembler;

@Configuration
public class DefaultBeanRegistry implements BeanRegistry {

    private static ApplicationContext applicationContext;

    @Override
    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        DefaultBeanRegistry.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static <T> T getBean(Class<T> requiredType) {
        return applicationContext.getBean(requiredType);
    }

    public static <T> T getBean(String name, Class<T> requiredType) {
        return applicationContext.getBean(name, requiredType);
    }

    public static PaginatedResourceAssembler getPageAssembler() {
        return applicationContext.getBean(PaginatedResourceAssembler.class);
    }
}
