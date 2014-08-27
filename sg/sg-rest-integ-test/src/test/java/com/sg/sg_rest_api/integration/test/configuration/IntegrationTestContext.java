package com.sg.sg_rest_api.integration.test.configuration;

import com.sg.domain.spring.configuration.SgCryptoContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
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
@Import({SgCryptoContext.class})
@PropertySource("classpath:/com/sg/configuration/properties/${com.sg.environment}/rest_api.integration.test.properties")
public class IntegrationTestContext {
    
    @Bean
    public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer() {
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        return configurer;
    }
    
    @Bean
    public IntegrationTestInitializer integrationTestInitializer()
    {
        return new IntegrationTestInitializer();
    }
}
