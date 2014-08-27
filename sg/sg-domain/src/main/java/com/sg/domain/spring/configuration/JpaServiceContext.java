package com.sg.domain.spring.configuration;

import com.sg.domain.service.JpaServiceImpl;
import com.sg.domain.service.SgService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author tarasev
 */
@Configuration
@PropertySource("classpath:/com/sg/configuration/properties/${com.sg.environment:test}/rest_api.installation.properties")
public class JpaServiceContext {

    @Bean
    public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer() {
        
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        return configurer;
    }
    
    @Bean
    public static SgService service() {
        return new JpaServiceImpl();
    }

}
