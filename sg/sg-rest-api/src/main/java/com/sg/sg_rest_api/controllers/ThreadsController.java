/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.sg_rest_api.controllers;

import com.sg.domain.dto.ThreadDto;
import com.sg.domain.dto.ThreadRefDto;
import com.sg.domain.service.SgService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ThreadsController {
    
    @Autowired
    SgService service;
    
    @RequestMapping(value = RequestPath.REQUEST_THREAD_ADD, method = RequestMethod.POST)
    public @ResponseBody void addThread(@RequestBody ThreadDto dto) {
        service.createThread(dto);
    }
    
    @RequestMapping(value = RequestPath.REQUEST_THREAD_LIST, method = RequestMethod.GET)
    public @ResponseBody List<ThreadDto> getThreads() {
        return service.listThreads();
    }
    
    @RequestMapping(value = RequestPath.REQUEST_THREAD_DELETE, method = RequestMethod.PUT)
    public @ResponseBody void deleteThread(@RequestBody ThreadRefDto dto) {
        service.deleteThread(dto);
    }
}
