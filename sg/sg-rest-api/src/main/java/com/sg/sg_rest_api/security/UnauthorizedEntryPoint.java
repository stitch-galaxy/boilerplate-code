/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.sg_rest_api.security;

/**
 *
 * @author tarasev
 */
import com.sg.dto.response.ErrorDto;
import com.sg.sg_rest_api.utils.CustomMediaTypes;
import com.sg.sg_rest_api.utils.Utils;
import java.io.IOException;
import javax.servlet.ServletException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * {@link AuthenticationEntryPoint} that rejects all requests. Login-like
 * function is featured in {@link TokenAuthenticationFilter} and this does not
 * perform or suggests any redirection. This object is hit whenever user is not
 * authorized (anonymous) and secured resource is requested.
 */
@Component
public class UnauthorizedEntryPoint extends BasicAuthenticationEntryPoint {

    public final static String REALM_NAME = "realm";

    private static final Logger LOGGER = LoggerFactory.getLogger(UnauthorizedEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authEx) throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(CustomMediaTypes.APPLICATION_JSON_UTF8.toString());
        
        ErrorDto dto = Utils.logExceptionAndCreateErrorDto(LOGGER, authEx);

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getWriter(), dto);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        setRealmName(REALM_NAME);
        super.afterPropertiesSet();
    }
}
