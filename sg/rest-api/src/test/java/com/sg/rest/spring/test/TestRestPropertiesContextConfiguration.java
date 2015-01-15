/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.rest.spring.test;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 *
 * @author tarasev
 */
@Configuration
@PropertySource("classpath:/com/sg/conf/rest.test.properties")
public class TestRestPropertiesContextConfiguration {

    @Bean
    public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer() {

        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        return configurer;
    }
}
