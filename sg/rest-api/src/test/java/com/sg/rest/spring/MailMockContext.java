package com.sg.rest.spring;

import com.sg.rest.mail.service.SgMailService;
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
public class MailMockContext {
    
    @Bean
    public SgMailService sgMailService() {
        return Mockito.mock(SgMailService.class);
    }
}
