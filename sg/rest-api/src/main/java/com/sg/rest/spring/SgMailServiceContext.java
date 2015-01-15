/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.rest.spring;

import com.sg.rest.mail.service.SgMailServiceImpl;
import com.sg.mail.service.EmailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author tarasev
 */
@Configuration
public class SgMailServiceContext {
    @Bean
    public EmailService sgMailService() {
        return new SgMailServiceImpl();
    }
}
