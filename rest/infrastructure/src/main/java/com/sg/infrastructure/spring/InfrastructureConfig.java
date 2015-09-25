/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.infrastructure.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 *
 * @author Admin
 */
@Configuration
@Import(value = {DomainEventsRoutingConfig.class})
public class InfrastructureConfig {
    
}
