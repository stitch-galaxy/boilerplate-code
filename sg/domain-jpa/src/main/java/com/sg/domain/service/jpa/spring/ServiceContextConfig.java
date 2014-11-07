package com.sg.domain.service.jpa.spring;

import com.sg.domain.service.jpa.components.ServiceImpl;
import com.sg.domain.service.SgService;
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
public class ServiceContextConfig {

    @Bean
    public static SgService service() {
        return new ServiceImpl();
    }

}
