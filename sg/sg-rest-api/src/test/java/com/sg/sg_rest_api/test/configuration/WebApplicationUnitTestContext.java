/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.sg_rest_api.test.configuration;

import com.sg.domain.spring.configuration.JacksonMapperContext;
import com.sg.sg_rest_api.test.configuration.ServiceMockContext;
import com.sg.domain.spring.configuration.ValidatorContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 *
 * @author tarasev
 */
@Configuration
@EnableWebMvc
@Import({ValidatorContext.class, ServiceMockContext.class, CryptoMockContext.class, JacksonMapperContext.class, MailMockContext.class})
public class WebApplicationUnitTestContext extends WebMvcConfigurerAdapter {
    
}
