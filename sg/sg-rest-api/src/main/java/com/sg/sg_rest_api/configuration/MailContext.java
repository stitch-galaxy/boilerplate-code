/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.sg_rest_api.configuration;

import com.sg.sg_rest_api.mail.GaeMailServiceImpl;
import com.sg.sg_rest_api.mail.MailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author tarasev
 */
@Configuration
public class MailContext {
    @Bean
    public MailService mailService() {
        return new GaeMailServiceImpl();
    }
}
