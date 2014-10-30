/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.spring.configuration;

import com.sg.domain.entities.jpa.CanvasesRepository;
import com.sg.domain.entities.jpa.ProductRepository;
import com.sg.domain.entities.jpa.ThreadsRepository;
import java.util.Properties;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
//import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.HibernateExceptionTranslator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

/**
 *
 * @author tarasev
 */
@Configuration
@EnableJpaRepositories(basePackageClasses = {ThreadsRepository.class, CanvasesRepository.class, ProductRepository.class})
//GAE do not support @Transactional annotation because where is a class on a call stack which is not in a white list
//@EnableTransactionManagement
public class JpaContext {

    @Value("${jdbc.url}")
    private String jdbcUrl;
    @Value("${jdbc.driverClassName}")
    private String driverClassName;
    @Value("${jdbc.username}")
    private String username;
    @Value("${jdbc.password}")
    private String password;
    @Value("${hibernate.dialect}")
    private String hibernateDialect;
    @Value("${hibernate.hbm2ddl.auto:}")
    private String hibernateHbm2ddlAuto;
    @Value("${hibernate.show_sql:false}")
    private Boolean hibernateShowSql;
    @Value("${hibernate.format_sql:false}")
    private Boolean hibernateFormatSql;

    @Bean
    public EntityManagerFactory entityManagerFactory() {

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("com.sg.domain.entities.jpa");
        factory.setDataSource(dataSource());

        Properties jpaProperties = new Properties();
        jpaProperties.put(org.hibernate.cfg.Environment.SHOW_SQL, hibernateShowSql);
        jpaProperties.put(org.hibernate.cfg.Environment.FORMAT_SQL, hibernateFormatSql);
        jpaProperties.put(org.hibernate.cfg.Environment.DIALECT, hibernateDialect);
        if (hibernateHbm2ddlAuto != null && !hibernateHbm2ddlAuto.isEmpty()) {
            jpaProperties.put(org.hibernate.cfg.Environment.HBM2DDL_AUTO, hibernateHbm2ddlAuto);
        }

        factory.setJpaProperties(jpaProperties);
        
        factory.afterPropertiesSet();
        EntityManagerFactory result = factory.getObject();
        return result;
    }
    
    @Bean DataSource dataSource()
    {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName(driverClassName);
        ds.setUrl(jdbcUrl);
        ds.setUsername(username);
        ds.setPassword(password);
        return ds;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory());
        return txManager;
    }

    @Bean
    public TransactionTemplate transactionTemplate() {
        TransactionTemplate template = new TransactionTemplate(transactionManager());
        template.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        return template;
    }

    @Bean
    public HibernateExceptionTranslator hibernateExceptionTranslator() {
        return new HibernateExceptionTranslator();
    }
}
