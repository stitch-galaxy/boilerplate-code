/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.infrastructure.spring;

import com.sg.infrastructure.InfrastructureNoOp;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author Admin
 */
@Configuration
@ComponentScan(basePackageClasses = {InfrastructureNoOp.class})
public class InfrastructureServicesConfig {
    
}
