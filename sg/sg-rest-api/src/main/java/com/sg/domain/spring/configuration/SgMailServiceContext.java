/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.domain.spring.configuration;

import com.sg.domain.service.GaeMailServiceImpl;
import com.sg.domain.service.SgMailService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author tarasev
 */
@Configuration
public class SgMailServiceContext {
    @Bean
    public SgMailService sgMailService() {
        return new GaeMailServiceImpl();
    }
}
