/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.sg_rest_api.controllers;

import com.sg.dto.ThreadDto;
import com.sg.dto.ThreadRefDto;
import com.sg.dto.ThreadUpdateDto;
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
    public @ResponseBody void create(@RequestBody ThreadDto dto) {
        service.create(dto);
    }
    
    @RequestMapping(value = RequestPath.REQUEST_THREAD_LIST, method = RequestMethod.GET)
    public @ResponseBody List<ThreadDto> list() {
        List<ThreadDto> result = service.listThreads();
        return result;
    }
    
    @RequestMapping(value = RequestPath.REQUEST_THREAD_DELETE, method = RequestMethod.PUT)
    public @ResponseBody void delete(@RequestBody ThreadRefDto dto) {
        service.delete(dto);
    }
    
    @RequestMapping(value = RequestPath.REQUEST_THREAD_UPDATE, method = RequestMethod.PUT)
    public @ResponseBody void update(@RequestBody ThreadUpdateDto dto) {
        service.update(dto);
    }
}
