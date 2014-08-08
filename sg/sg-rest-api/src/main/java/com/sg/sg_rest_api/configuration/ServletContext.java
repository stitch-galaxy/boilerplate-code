/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.sg_rest_api.configuration;

import com.sg.sg_rest_api.controllers.CanvasesController;
import com.sg.sg_rest_api.controllers.ThreadsController;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 *
 * @author tarasev
 */
@Configuration
@EnableWebMvc
//@ComponentScan(basePackageClasses = {CanvasesController.class, ThreadsController.class})
@ComponentScan("com.sg.sg_rest_api.controllers")
public class ServletContext extends WebMvcConfigurerAdapter {
}
