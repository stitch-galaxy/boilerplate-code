/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.rest.spring;

import com.sg.rest.spring.JacksonMapperContext;
import com.sg.rest.spring.WebTokenServiceContextConfiguration;
import com.sg.rest.spring.SecurityContext;
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
@Import({TestRestPropertiesContextConfiguration.class, ServiceMockContext.class, DomainComponentsMockContext.class, WebTokenServiceContextConfiguration.class, JacksonMapperContext.class, SecurityContext.class, MailMockContext.class})
public class WebApplicationIntegrationTestContext extends WebMvcConfigurerAdapter {
    
}
