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
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

/**
 * {@link AuthenticationEntryPoint} that rejects all requests. Login-like
 * function is featured in {@link TokenAuthenticationFilter} and this does not
 * perform or suggests any redirection. This object is hit whenever user is not
 * authorized (anonymous) and secured resource is requested.
 */
public final class UnauthorizedEntryPoint extends BasicAuthenticationEntryPoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(UnauthorizedEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        //TODO: log properly
        //TODO: prepare correct json response
        //TODO: http://stackoverflow.com/questions/19102095/overriding-container-response-for-spring-security-badcredentialsexception
        //LOGGER.debug(" *** UnauthorizedEntryPoint.commence: " + request.getRequestURI());
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("{\"status\":" + HttpServletResponse.SC_UNAUTHORIZED + ", \"message\":\"" + authException.getMessage() + "\"}");
        //response.getWriter().flush();
        //response.getWriter().close();
    }
}
