/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.sg_rest_api.controllers;

import javax.servlet.http.HttpServletResponse;
//import org.springframework.config.java.context.JavaConfigWebApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OptionsController {

    @RequestMapping(method = RequestMethod.OPTIONS, value = "/**")
    public void loginOptions(HttpServletResponse response) {
        //new JavaConfigWebApplicationContext();
    }
}

