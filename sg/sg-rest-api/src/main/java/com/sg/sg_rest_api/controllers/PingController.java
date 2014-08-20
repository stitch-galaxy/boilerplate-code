/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.sg_rest_api.controllers;

import com.sg.constants.OperationStatus;
import com.sg.constants.RequestPath;
import com.sg.constants.ThreadOperationStatus;
import com.sg.dto.ThreadDto;
import com.sg.dto.ThreadRefDto;
import com.sg.dto.ThreadUpdateDto;
import com.sg.domain.service.SgService;
import com.sg.domain.service.exception.SgThreadAlreadyExistsException;
import com.sg.domain.service.exception.SgThreadNotFoundException;
import com.sg.dto.OperationStatusDto;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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
    public @ResponseBody
    OperationStatusDto ping() throws Exception {
        service.ping();
        OperationStatusDto result = new OperationStatusDto();
        result.setStatus(OperationStatus.STATUS_SUCCESS);
        return result;
    }
    
    @RequestMapping(value = RequestPath.REQUEST_SECURE_PING, method = RequestMethod.GET)
    public @ResponseBody
    OperationStatusDto securePing() throws Exception {
        service.ping();
        OperationStatusDto result = new OperationStatusDto();
        result.setStatus(OperationStatus.STATUS_SUCCESS);
        return result;
    }

    
}
