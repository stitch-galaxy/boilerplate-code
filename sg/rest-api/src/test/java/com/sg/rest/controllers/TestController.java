/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.rest.controllers;

import com.sg.rest.apipath.RequestPath;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @RequestMapping(value = RequestPath.TEST_REQUEST, method = RequestMethod.GET)
    public void ping() throws Exception {
    }
    
    @RequestMapping(value = RequestPath.TEST_REQUEST_THROW_EXCEPTION, method = RequestMethod.GET)
    public void pingException() throws Exception {
        throw new Exception("Error");
    }

    @RequestMapping(value = RequestPath.TEST_SECURE_REQUEST, method = RequestMethod.GET)
    public void securePing() throws Exception {
    }
}
