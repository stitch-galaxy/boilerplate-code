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
import java.io.PrintWriter;
import javax.servlet.ServletException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(UnauthorizedEntryPoint.class);

//    @Override
//    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
//        //TODO: log properly
//        //TODO: prepare correct json response
//        //TODO: http://stackoverflow.com/questions/19102095/overriding-container-response-for-spring-security-badcredentialsexception
//        //LOGGER.debug(" *** UnauthorizedEntryPoint.commence: " + request.getRequestURI());
//        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
//        response.getWriter().println("{\"status\":" + HttpServletResponse.SC_UNAUTHORIZED + ", \"message\":\"" + authException.getMessage() + "\"}");
//        //response.getWriter().flush();
//        //response.getWriter().close();
//    }
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authEx) throws IOException, ServletException {
        //TODO: log properly
        //response.addHeader("WWW-Authenticate", "Basic realm=\"" + getRealmName() + "\"");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        PrintWriter writer = response.getWriter();
        writer.println("HTTP Status 401 - " + authEx.getMessage());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        setRealmName("Stitch galaxy");
        super.afterPropertiesSet();
    }
}
