/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.sg_rest_api.configuration;

import com.sg.sg_rest_api.security.Security;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 *
 * @author tarasev
 */
@Configuration
@PropertySource("classpath:/com/sg/configuration/properties/${com.sg.environment}/security.properties")
public class CryptographyContext {

    @Bean
    public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer() {

        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        return configurer;
    }

    @Bean
    public Security security() {
        return new Security();
    }
}
