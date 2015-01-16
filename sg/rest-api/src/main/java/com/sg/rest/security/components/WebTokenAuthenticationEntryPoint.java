/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.rest.security.components;

/**
 *
 * @author tarasev
 */
import com.sg.rest.dto.AuthenficationFailed;
import com.sg.rest.dto.AuthentificationFailureStatus;
import com.sg.rest.webtoken.WebSecurityAccountNotFoundException;
import com.sg.rest.webtoken.WebSecurityBadTokenException;
import com.sg.rest.webtoken.WebSecurityTokenExpiredException;
import com.sg.rest.utils.CustomMediaTypes;
import java.io.IOException;
import javax.servlet.ServletException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InsufficientAuthenticationException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * {@link AuthenticationEntryPoint} that rejects all requests. Login-like
 * function is featured in {@link TokenAuthenticationFilter} and this does not
 * perform or suggests any redirection. This object is hit whenever user is not
 * authorized (anonymous) and secured resource is requested.
 */
@Component
public class WebTokenAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebTokenAuthenticationEntryPoint.class);
    
    @Autowired
    private ObjectMapper jacksonObjectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authEx) throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        
        AuthenficationFailed dto;
        if (authEx instanceof WebSecurityAccountNotFoundException) {
            dto = new AuthenficationFailed(AuthentificationFailureStatus.TOKEN_AUTHENTICATION_ACCOUNT_DO_NOT_EXISTS);
            //dto.setErrorCode(ErrorCodes.TOKEN_AUTHENTICATION_ACCOUNT_DO_NOT_EXISTS);
        }
        else if (authEx instanceof WebSecurityBadTokenException) {
            dto = new AuthenficationFailed(AuthentificationFailureStatus.TOKEN_AUTHENTICATION_BAD_TOKEN);
            //dto.setErrorCode(ErrorCodes.TOKEN_AUTHENTICATION_BAD_TOKEN);
        }
        else if (authEx instanceof WebSecurityTokenExpiredException) {
            dto = new AuthenficationFailed(AuthentificationFailureStatus.TOKEN_AUTHENTICATION_TOKEN_EXPIRED);
            //dto.setErrorCode(ErrorCodes.TOKEN_AUTHENTICATION_TOKEN_EXPIRED);
        }
        else if (authEx instanceof InsufficientAuthenticationException)
        {
            dto = new AuthenficationFailed(AuthentificationFailureStatus.TOKEN_AUTHENTICATION_NO_TOKEN);
            //dto.setErrorCode(ErrorCodes.TOKEN_AUTHENTICATION_NO_TOKEN);
        }
        else
        {
            dto = new AuthenficationFailed(AuthentificationFailureStatus.UNKNOWN);
        }
        
        LOGGER.error("Authentication failed " + dto.getEventRef().getId() + ": ", authEx);
        
        response.setContentType(CustomMediaTypes.APPLICATION_JSON_UTF8.toString());
        jacksonObjectMapper.writeValue(response.getWriter(), dto);
    }
}
