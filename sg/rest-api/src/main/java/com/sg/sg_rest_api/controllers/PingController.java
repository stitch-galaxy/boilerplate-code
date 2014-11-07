/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.sg_rest_api.controllers;

import com.sg.constants.RequestPath;
import com.sg.domain.service.SgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {

    @Autowired
    SgService service;

    @RequestMapping(value = RequestPath.REQUEST_PING, method = RequestMethod.GET)
    public void ping() throws Exception {
        service.ping();
    }

    @RequestMapping(value = RequestPath.REQUEST_SECURE_PING, method = RequestMethod.GET)
    public void securePing() throws Exception {
        service.ping();
    }
}
