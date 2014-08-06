/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.sg_rest_api.configuration;

import com.sg.domain.spring.configuration.JpaConfig;
import com.sg.domain.spring.configuration.JpaServiceConfig;
import com.sg.domain.spring.configuration.MapperConfig;
import com.sg.sg_rest_api.controllers.CanvasesController;
import com.sg.sg_rest_api.controllers.ThreadsController;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 *
 * @author tarasev
 */
//http://www.mkyong.com/spring-security/spring-security-hello-world-annotation-example/
//http://www.luckyryan.com/2013/02/07/migrate-spring-mvc-servlet-xml-to-java-config/
//http://www.robinhowlett.com/blog/2013/02/13/spring-app-migration-from-xml-to-java-based-config/
@Configuration
@EnableWebMvc
@ComponentScan(basePackageClasses = {CanvasesController.class, ThreadsController.class})
//@ComponentScan("com.sg.sg_rest_api.controllers")
@Import({JpaConfig.class, MapperConfig.class, JpaServiceConfig.class})
public class WebConfig extends WebMvcConfigurerAdapter {
    
}
