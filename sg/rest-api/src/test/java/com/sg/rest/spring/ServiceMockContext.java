package com.sg.rest.spring;

import com.sg.domain.service.SgService;
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
public class ServiceMockContext {
    @Bean
    public SgService service() {
        return Mockito.mock(SgService.class);
    }
    
}
