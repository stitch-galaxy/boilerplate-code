/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.stitchgalaxy.sg_rest_api.configuration;

import com.stitchgalaxy.domain.spring.configuration.JpaConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 *
 * @author tarasev
 */
@Configuration
@EnableWebMvc
@ComponentScan({/*"com.stitchgalaxy.sg_rest_api.configuration",*/"com.stitchgalaxy.sg_rest_api.controllers"})
@Import(JpaConfig.class)
public class WebConfig {
    
}
