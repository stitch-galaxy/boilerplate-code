/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.sg_rest_api.controllers;

import com.sg.constants.RequestPath;
import com.sg.dto.request.CanvasCreateDto;
import com.sg.dto.request.CanvasDeleteDto;
import com.sg.dto.request.CanvasUpdateDto;
import com.sg.domain.service.SgService;
import com.sg.domain.service.exception.SgDataValidationException;
import com.sg.dto.response.CanvasesListDto;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CanvasesController {
    
    @Autowired
    SgService service;
    
    @RequestMapping(value = RequestPath.REQUEST_CANVAS_ADD, method = RequestMethod.POST)
    public @ResponseBody void create(@Valid @RequestBody CanvasCreateDto dto) throws SgDataValidationException {
        service.create(dto);
    }
    
    @RequestMapping(value = RequestPath.REQUEST_CANVAS_LIST, method = RequestMethod.GET)
    public @ResponseBody CanvasesListDto list() {
        return service.listCanvases();
    }
    
    @RequestMapping(value = RequestPath.REQUEST_CANVAS_DELETE, method = RequestMethod.POST)
    public @ResponseBody void delete(@Valid @RequestBody CanvasDeleteDto dto) throws SgDataValidationException {
        service.delete(dto);
    }
    
    @RequestMapping(value = RequestPath.REQUEST_CANVAS_UPDATE, method = RequestMethod.POST)
    public @ResponseBody void update(@Valid @RequestBody CanvasUpdateDto dto) throws SgDataValidationException {
        service.update(dto);
    }
}
