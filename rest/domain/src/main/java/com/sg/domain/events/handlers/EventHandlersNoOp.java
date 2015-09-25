package com.sg.domain.events.handlers;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Admin
 */
@Configuration
@ComponentScan(basePackageClasses = {EventHandlersNoOp.class, })
public class EventHandlersNoOp {
    
}
