/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.jpa.spring;

import com.sg.domain.jpa.repository.NoOpClass;
import com.sg.domain.jpa.spring.components.mapper.MapperComponent;
import java.util.Properties;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.HibernateExceptionTranslator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 *
 * @author tarasev
 */
@Configuration
@EnableJpaRepositories(basePackageClasses = {NoOpClass.class, MapperComponent.class})
@EnableTransactionManagement
public class PersistenceContextConfig {

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

    //http://blog.ippon.fr/2014/09/29/spring-javaconfig-tips-les-factorybean/
    //http://stackoverflow.com/questions/19747789/beandefinitionstoreexception-exception-since-trying-to-switch-app-to-javaconfig
    //http://forum.spring.io/forum/spring-projects/container/45760-correct-usage-of-factorybeans-in-javaconfig
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("com.sg.domain.jpa.entities");
        factory.setDataSource(dataSource);

        Properties jpaProperties = new Properties();
        jpaProperties.put(org.hibernate.cfg.Environment.SHOW_SQL, hibernateShowSql);
        jpaProperties.put(org.hibernate.cfg.Environment.FORMAT_SQL, hibernateFormatSql);
        jpaProperties.put(org.hibernate.cfg.Environment.DIALECT, hibernateDialect);
        if (hibernateHbm2ddlAuto != null && !hibernateHbm2ddlAuto.isEmpty()) {
            jpaProperties.put(org.hibernate.cfg.Environment.HBM2DDL_AUTO, hibernateHbm2ddlAuto);
        }

        factory.setJpaProperties(jpaProperties);
        
        return factory;
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
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf)
    {
        return new JpaTransactionManager(emf);
    }

    @Bean
    public HibernateExceptionTranslator hibernateExceptionTranslator() {
        return new HibernateExceptionTranslator();
    }
}
