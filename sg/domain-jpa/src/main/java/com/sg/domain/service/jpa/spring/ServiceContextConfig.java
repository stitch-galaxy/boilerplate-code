package com.sg.domain.service.jpa.spring;

import com.sg.domain.service.jpa.components.SgServiceImpl;
import com.sg.domain.service.mapping.components.MapperComponent;
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
@ComponentScan(basePackageClasses = {SgServiceImpl.class, MapperComponent.class})
public class ServiceContextConfig {
}
