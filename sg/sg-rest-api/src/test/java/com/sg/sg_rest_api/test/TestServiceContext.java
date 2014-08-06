package com.sg.sg_rest_api.test;

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
public class TestServiceContext {
    @Bean
    public SgService todoService() {
        return Mockito.mock(SgService.class);
    }
    
}
