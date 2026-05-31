/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.htweb.configs;

import jakarta.servlet.MultipartConfigElement;
import jakarta.servlet.ServletRegistration;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * @author PC
 */
public class DispatcherServletInit extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{
                ThymeleafConfigs.class,
                HibernateConfigs.class,
                SpringSecurityConfigs.class,
                RedisConfig.class,
                SwaggerConfig.class,
                AsyncConfig.class,
                StorageConfig.class,
                WebSocketConfig.class
        };
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{
                WebAppContextConfigs.class
        };
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    @Override
    protected void customizeRegistration(ServletRegistration.Dynamic registration) {
        registration.setAsyncSupported(true);

        registration.setMultipartConfig(
                new MultipartConfigElement(
                        null,
                        10 * 1024 * 1024,   // max file size: 10MB
                        20 * 1024 * 1024,   // max request size: 20MB
                        0                   // file size threshold
                )
        );
    }
}
