/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stitchgalaxy.domain.test;

import com.stitchgalaxy.domain.entities.jpa.ProductRepository;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.hibernate4.HibernateExceptionTranslator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

/**
 *
 * @author tarasev
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.stitchgalaxy.domain.entities.jpa")
@ComponentScan(basePackages = "com.stitchgalaxy.domain.service")
@EnableTransactionManagement
@PropertySource("classpath:/com.stitchgalaxy.domain.persistence.jpa.properties")
public class JpaConfig {
    
    @Value( "${jdbc.url}" ) private String jdbcUrl;
    @Value( "${jdbc.driverClassName}" ) private String driverClassName;
    @Value( "${jdbc.username}" ) private String username;
    @Value( "${jdbc.password}" ) private String password;
    @Value( "${hibernate.dialect}" ) private String hibernateDialect;
    @Value( "${hibernate.hbm2ddl.auto}" ) private String hibernateHbm2ddlAuto;

    @Bean
    public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
    
    @Bean
    public EntityManagerFactory entityManagerFactory() {

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("com.stitchgalaxy.domain.entities.jpa");
        factory.setDataSource(dataSource());
        //no caching
        factory.getJpaPropertyMap().put("cache.provider_class", "org.hibernate.cache.NoCacheProvider");
        //show and format sql during logging
        factory.getJpaPropertyMap().put("hibernate.show_sql", true);
        factory.getJpaPropertyMap().put("hibernate.format_sql", true);
        //dialect
        factory.getJpaPropertyMap().put("hibernate.dialect", hibernateDialect);
        //db schema
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
        //EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        //return builder.setType(EmbeddedDatabaseType.HSQL).build();
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory());
        return txManager;
    }
    
    @Bean 
    public HibernateExceptionTranslator hibernateExceptionTranslator(){ 
      return new HibernateExceptionTranslator(); 
    }
}
