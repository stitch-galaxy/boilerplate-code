/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.rest.spring.test;

import com.sg.rest.spring.test.MailMockContext;
import com.sg.rest.spring.test.DomainComponentsMockContext;
import com.sg.rest.spring.JacksonMapperContext;
import com.sg.rest.spring.JacksonMapperContext;
import com.sg.rest.spring.JacksonMapperContext;
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
@Import({DomainComponentsMockContext.class, WebSecurityServiceMockContextConfiguration.class, JacksonMapperContext.class, MailMockContext.class})
public class WebApplicationUnitTestContext extends WebMvcConfigurerAdapter {
    
}
