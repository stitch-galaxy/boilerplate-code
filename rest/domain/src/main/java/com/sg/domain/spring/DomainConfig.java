/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.spring;

import com.sg.domain.events.handlers.EventHandlersNoOp;
import com.sg.domain.services.DomainServicesNoOp;
import com.sg.domain.specs.SpecificationsNoOp;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author Admin
 */
@Configuration
@ComponentScan(basePackageClasses = {
    EventHandlersNoOp.class,
    DomainServicesNoOp.class,
    SpecificationsNoOp.class,
})
public class DomainConfig {
    
    
    
}
