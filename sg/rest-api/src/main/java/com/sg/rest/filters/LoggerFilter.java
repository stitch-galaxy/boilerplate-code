/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.rest.filters;

/**
 *
 * @author tarasev
 */
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LoggerFilter implements Filter {

    private static final String BODY = "Body: ";
    private static final String PRINCIPAL = "Principal: ";
    private static final String REMOTE_HOST = "RemoteHost: ";
    private static final String REMOTE_ADDR = "RemoteAddr: ";
    private static final String COOKIES = "Cookies: ";
    private static final String AUTH_TYPE = "AuthType: ";
    private static final String HEADERS = "Headers: ";
    private static final String ATTRIBUTES = "Attributes: ";
    private static final String PARAMETERS = "Parameters: ";
    private static final String METHOD = "Method: ";
    private static final String REQUEST_URL = "Request url: ";
    private static final String INCOMING_REQUEST = "Incoming request:";
    private static final String NON_HTTP_REQUEST = "Non http request: ";
    private static final String EXPECTING_AN_HTTP_REQUEST = "Expecting an HTTP request";

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggerFilter.class);

    public void destroy() {
        // Nothing to do
    }

    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        if (!(request instanceof HttpServletRequest)) {
            LOGGER.error(NON_HTTP_REQUEST + System.lineSeparator() + request.getInputStream().toString());
            throw new RuntimeException(EXPECTING_AN_HTTP_REQUEST);
        }

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        StringBuilder sb = new StringBuilder();
        //General header
        sb.append(System.lineSeparator());
        sb.append(INCOMING_REQUEST);
        //Request url
        sb.append(System.lineSeparator());
        sb.append(REQUEST_URL);
        sb.append(httpRequest.getRequestURL());
        //Method
        sb.append(System.lineSeparator());
        sb.append(METHOD);
        sb.append(httpRequest.getMethod());
        //Parameters
        if (httpRequest.getParameterNames().hasMoreElements()) {
            sb.append(System.lineSeparator());
            sb.append(PARAMETERS);
            Enumeration enParams = httpRequest.getParameterNames();
            while (enParams.hasMoreElements()) {
                sb.append(System.lineSeparator());
                String paramName = (String) enParams.nextElement();
                sb.append(paramName);
                sb.append(" : ");
                sb.append(httpRequest.getParameter(paramName));
            }
        }
        //Attributes
        if (httpRequest.getAttributeNames().hasMoreElements()) {
            sb.append(System.lineSeparator());
            sb.append(ATTRIBUTES);
            Enumeration enAttribs = httpRequest.getAttributeNames();
            while (enAttribs.hasMoreElements()) {
                sb.append(System.lineSeparator());
                String attribName = (String) enAttribs.nextElement();
                sb.append(attribName);
                sb.append(" : ");
                sb.append(httpRequest.getAttribute(attribName));
            }
        }
        //Headers
        if (httpRequest.getHeaderNames().hasMoreElements()) {
            sb.append(System.lineSeparator());
            sb.append(HEADERS);
            Enumeration enHeaders = httpRequest.getHeaderNames();
            while (enHeaders.hasMoreElements()) {
                sb.append(System.lineSeparator());
                String headerName = (String) enHeaders.nextElement();
                sb.append(headerName);
                sb.append(" : ");
                sb.append(httpRequest.getHeader(headerName));
            }
        }
        //AuthType
        if (httpRequest.getAuthType() != null && !httpRequest.getAuthType().isEmpty()) {
            sb.append(System.lineSeparator());
            sb.append(AUTH_TYPE);
            sb.append(httpRequest.getAuthType());
        }
        //Cookies
        if (httpRequest.getCookies() != null && httpRequest.getCookies().length > 0) {
            sb.append(System.lineSeparator());
            sb.append(COOKIES);
            for (Cookie cookie : httpRequest.getCookies()) {
                sb.append(System.lineSeparator());
                sb.append(cookie.getName());
                sb.append(" : ");
                sb.append(cookie.getValue());
            }
        }
        //RemoteAddr
        if (httpRequest.getRemoteAddr() != null && !httpRequest.getRemoteAddr().isEmpty()) {
            sb.append(System.lineSeparator());
            sb.append(REMOTE_ADDR);
            sb.append(httpRequest.getRemoteAddr());
        }
        //RemoteHost
        if (httpRequest.getRemoteHost() != null && !httpRequest.getRemoteHost().isEmpty()) {
            sb.append(System.lineSeparator());
            sb.append(REMOTE_HOST);
            sb.append(httpRequest.getRemoteHost());
        }
        //User principal
        if (httpRequest.getUserPrincipal() != null) {
            if (httpRequest.getUserPrincipal().getName() != null && !httpRequest.getUserPrincipal().getName().isEmpty()) {
                sb.append(System.lineSeparator());
                sb.append(PRINCIPAL);
                sb.append(httpRequest.getUserPrincipal().getName());
            }
        }
        //Body
        ResettableStreamHttpServletRequest wrappedRequest = new ResettableStreamHttpServletRequest((HttpServletRequest) request);
        String body = IOUtils.toString(wrappedRequest.getReader());
        if (body != null && !body.isEmpty()) {
            sb.append(System.lineSeparator());
            sb.append(BODY);
            sb.append(System.lineSeparator());
            sb.append(body);
        }
        wrappedRequest.resetInputStream();

        LOGGER.info(sb.toString());
        chain.doFilter(wrappedRequest, response);
    }

    public void init(FilterConfig arg0) throws ServletException {
        // Nothing to do
    }

    private static class ResettableStreamHttpServletRequest extends
            HttpServletRequestWrapper {

        private byte[] rawData;
        private HttpServletRequest request;
        private ResettableServletInputStream servletStream;

        public ResettableStreamHttpServletRequest(HttpServletRequest request) {
            super(request);
            this.request = request;
            this.servletStream = new ResettableServletInputStream();
        }

        public void resetInputStream() {
            servletStream.stream = new ByteArrayInputStream(rawData);
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {
            if (rawData == null) {
                rawData = IOUtils.toByteArray(this.request.getReader());
                servletStream.stream = new ByteArrayInputStream(rawData);
            }
            return servletStream;
        }

        @Override
        public BufferedReader getReader() throws IOException {
            if (rawData == null) {
                rawData = IOUtils.toByteArray(this.request.getReader());
                servletStream.stream = new ByteArrayInputStream(rawData);
            }
            return new BufferedReader(new InputStreamReader(servletStream));
        }

        private class ResettableServletInputStream extends ServletInputStream {

            private InputStream stream;

            @Override
            public int read() throws IOException {
                return stream.read();
            }
        }
    }
}
