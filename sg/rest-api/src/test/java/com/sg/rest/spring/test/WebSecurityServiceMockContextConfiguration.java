package com.sg.rest.spring.test;

import com.sg.rest.webtoken.WebTokenService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
public class WebSecurityServiceMockContextConfiguration {
    @Bean
    public WebTokenService sgCryptoService() {
        return Mockito.mock(WebTokenService.class);
    }
}
