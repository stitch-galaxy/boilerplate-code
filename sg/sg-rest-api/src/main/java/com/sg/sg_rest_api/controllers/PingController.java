/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.sg_rest_api.controllers;

import com.sg.constants.OperationStatus;
import com.sg.constants.RequestPath;
import com.sg.constants.ThreadOperationStatus;
import com.sg.dto.request.ThreadCreateDto;
import com.sg.dto.request.ThreadDeleteDto;
import com.sg.dto.request.ThreadUpdateDto;
import com.sg.domain.service.SgService;
import com.sg.domain.service.exception.SgThreadAlreadyExistsException;
import com.sg.domain.service.exception.SgThreadNotFoundException;
import com.sg.dto.response.OperationStatusDto;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PingController {

    @Autowired
    SgService service;

    @RequestMapping(value = RequestPath.REQUEST_PING, method = RequestMethod.GET)
    public void ping() throws Exception {
        service.ping();
    }
    
    @RequestMapping(value = RequestPath.REQUEST_SECURE_PING, method = RequestMethod.GET)
    public @ResponseBody
    String securePing() throws Exception {
        service.ping();
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
    }

    
}
