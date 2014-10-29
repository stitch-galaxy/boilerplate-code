/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.spring.configuration;

import com.sg.domain.entities.jpa.CanvasesRepository;
import com.sg.domain.entities.jpa.ProductRepository;
import com.sg.domain.entities.jpa.ThreadsRepository;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
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
    
    @Value( "${jdbc.url}" ) private String jdbcUrl;
    @Value( "${jdbc.driverClassName}" ) private String driverClassName;
    @Value( "${jdbc.username}" ) private String username;
    @Value( "${jdbc.password}" ) private String password;
    @Value( "${hibernate.dialect}" ) private String hibernateDialect;
    @Value( "${hibernate.hbm2ddl.auto:}" ) private String hibernateHbm2ddlAuto;
    @Value( "${hibernate.show_sql:false}" ) private Boolean hibernateShowSql;
    @Value( "${hibernate.format_sql:false}" ) private Boolean hibernateFormatSql;      
    
    @Bean
    public EntityManagerFactory entityManagerFactory() {

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("com.sg.domain.entities.jpa");
        factory.setDataSource(dataSource());
        //no caching
        factory.getJpaPropertyMap().put("cache.provider_class", "org.hibernate.cache.NoCacheProvider");
        //show and format sql during logging
        factory.getJpaPropertyMap().put("hibernate.show_sql", hibernateShowSql);
        factory.getJpaPropertyMap().put("hibernate.format_sql", hibernateFormatSql);
        //dialect
        factory.getJpaPropertyMap().put("hibernate.dialect", hibernateDialect);
        //db schema
        if (hibernateHbm2ddlAuto != null && !hibernateHbm2ddlAuto.isEmpty())
            factory.getJpaPropertyMap().put("hibernate.hbm2ddl.auto", hibernateHbm2ddlAuto);
        factory.afterPropertiesSet();

        return factory.getObject();
    }
    
    @Bean
    public DataSource dataSource() {
        org.apache.commons.dbcp.BasicDataSource ds = new org.apache.commons.dbcp.BasicDataSource();
        ds.setUrl(jdbcUrl);
        ds.setDriverClassName(driverClassName);
        ds.setUsername(username);
        ds.setPassword(password);
        ds.setInitialSize(4);
        ds.setDefaultAutoCommit(false);
        return ds;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory());
        return txManager;
    }
    
    @Bean
    public TransactionTemplate transactionTemplate()
    {
        TransactionTemplate template = new TransactionTemplate(transactionManager());
        template.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        return template;
    }
    
    @Bean 
    public HibernateExceptionTranslator hibernateExceptionTranslator(){ 
      return new HibernateExceptionTranslator(); 
    }
}
