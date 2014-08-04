/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.stitchgalaxy.sg_rest_api.controllers;

import com.stitchgalaxy.domain.service.StitchGalaxyService;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {
    
    @Autowired
    StitchGalaxyService service;
    
    @RequestMapping(value = "/addProduct", method = RequestMethod.GET)
    public @ResponseBody String addUser(HttpServletResponse response) {
        return service.addProduct().toString();
    }
}
