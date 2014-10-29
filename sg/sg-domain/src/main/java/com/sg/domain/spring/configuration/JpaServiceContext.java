package com.sg.domain.spring.configuration;

import com.sg.domain.service.JpaServiceImpl;
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
public class JpaServiceContext {

    @Bean
    public static SgService service() {
        return new JpaServiceImpl();
    }

}
