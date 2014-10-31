/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.spring.configuration;

import com.sg.domain.service.SgCryptoService;
import com.sg.domain.service.SgCryptoServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 *
 * @author tarasev
 */
@Configuration
public class SgCryptoContext {

    @Bean
    public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer() {

        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        return configurer;
    }

    @Bean
    public SgCryptoService sgCryptoService() {
        return new SgCryptoServiceImpl();
    }
}
