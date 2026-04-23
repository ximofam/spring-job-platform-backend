/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.htweb.configs;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import javax.sql.DataSource;
import java.util.Properties;

import static org.hibernate.cfg.JdbcSettings.DIALECT;

/**
 * @author PC
 */
@Configuration
@PropertySource("classpath:application.properties")
@PropertySource(
        value = "classpath:database.${spring.database.active}.${spring.profiles.active}.properties",
        ignoreResourceNotFound = false
)
public class HibernateConfigs {

    @Autowired
    private Environment env;

    @Bean
    public LocalSessionFactoryBean getSessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setPackagesToScan(new String[]{"com.htweb.core.pojo"});
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
    }

    @Bean
    public DataSource dataSource() {
        String hibernateConnUrl = String.format("%s", getRequired("hibernate.connection.url"));

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(getRequired("hibernate.connection.driverClass"));
        dataSource.setUrl(hibernateConnUrl);
        dataSource.setUsername(getRequired("db.user"));
        dataSource.setPassword(getRequired("db.password"));
        return dataSource;
    }

    @Bean(initMethod = "migrate")
    @DependsOn("dataSource")
    public Flyway flyway() {
        String dbActive = env.getProperty("spring.database.active");

        return Flyway.configure()
                .dataSource(dataSource())
                .locations("classpath:db/migration/" + dbActive, "classpath:db/seed/" + dbActive)
                .baselineOnMigrate(true)
                .load();
    }

    private Properties hibernateProperties() {
        Properties props = new Properties();
        props.put(DIALECT, env.getProperty("hibernate.dialect"));
        return props;
    }

    @Bean
    public HibernateTransactionManager transactionManager() {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(getSessionFactory().getObject());
        return transactionManager;
    }

    private String getRequired(String key) {
        String value = env.getProperty(key);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing required property: " + key);
        }
        return value;
    }
}
