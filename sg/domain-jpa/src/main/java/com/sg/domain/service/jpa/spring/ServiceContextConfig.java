package com.sg.domain.service.jpa.spring;

import com.sg.domain.service.jpa.components.NoOpClass;
import org.springframework.context.annotation.ComponentScan;
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
@ComponentScan(basePackageClasses = {NoOpClass.class})
public class ServiceContextConfig {
}
