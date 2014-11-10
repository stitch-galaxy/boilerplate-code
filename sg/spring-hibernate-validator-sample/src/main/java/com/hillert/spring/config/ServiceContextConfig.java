/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hillert.spring.config;

import com.hillert.spring.validation.service.impl.NoOpClass;
import org.springframework.context.annotation.Configuration;
//import org.springframework.validation.Validator;
import org.springframework.context.annotation.ComponentScan;

/**
 *
 * @author tarasev
 */
@Configuration
@ComponentScan(basePackageClasses = {NoOpClass.class})
public class ServiceContextConfig {
}
