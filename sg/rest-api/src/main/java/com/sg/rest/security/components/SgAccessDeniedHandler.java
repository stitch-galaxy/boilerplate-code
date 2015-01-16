/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.rest.security.components;

import com.sg.rest.enumerations.ErrorCodes;
import com.sg.dto.response.ErrorDto;
import com.sg.rest.utils.CustomMediaTypes;
import com.sg.rest.utils.Utils;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;


/**
 *
 * @author tarasev
 */
@Component
public class SgAccessDeniedHandler implements AccessDeniedHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebTokenAuthenticationEntryPoint.class);
    
    @Autowired
    private ObjectMapper jacksonObjectMapper;
    
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        
        String refNumber = Utils.logException(LOGGER, e);
        
        ErrorDto dto = new ErrorDto();
        dto.setRefNumber(refNumber);
        dto.setError(e.getMessage());
        dto.setErrorCode(ErrorCodes.ACCESS_DENIED);
        
        response.setContentType(CustomMediaTypes.APPLICATION_JSON_UTF8.toString());
        jacksonObjectMapper.writeValue(response.getWriter(), dto);
    }

}
