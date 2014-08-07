/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.sg_rest_api.filters;

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

public class LoggerFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggerFilter.class);

    public void destroy() {
        // Nothing to do
    }

    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        ResettableStreamHttpServletRequest wrappedRequest = new ResettableStreamHttpServletRequest(
                (HttpServletRequest) request);
        StringBuilder sb = new StringBuilder();
        //General header
        sb.append(System.lineSeparator());
        sb.append("Incoming request:");
        //Request url
        sb.append(System.lineSeparator());
        sb.append("Request url: ");
        sb.append(wrappedRequest.getRequestURL());
        //Method
        sb.append(System.lineSeparator());
        sb.append("Method: ");
        sb.append(wrappedRequest.getMethod());
        //Parameters
        if (wrappedRequest.getParameterNames().hasMoreElements()) {
            sb.append(System.lineSeparator());
            sb.append("Parameters: ");
            Enumeration enParams = wrappedRequest.getParameterNames();
            while (enParams.hasMoreElements()) {
                sb.append(System.lineSeparator());
                String paramName = (String) enParams.nextElement();
                sb.append(paramName);
                sb.append(" : ");
                sb.append(wrappedRequest.getParameter(paramName));
            }
        }
        //Attributes
        if (wrappedRequest.getAttributeNames().hasMoreElements()) {
            sb.append(System.lineSeparator());
            sb.append("Attributes: ");
            Enumeration enAttribs = wrappedRequest.getAttributeNames();
            while (enAttribs.hasMoreElements()) {
                sb.append(System.lineSeparator());
                String attribName = (String) enAttribs.nextElement();
                sb.append(attribName);
                sb.append(" : ");
                sb.append(wrappedRequest.getAttribute(attribName));
            }
        }
        //Headers
        if (wrappedRequest.getClass().isInstance(HttpServletRequest.class)) {
            HttpServletRequest httpRequest = (HttpServletRequest) wrappedRequest;
            if (httpRequest.getHeaderNames().hasMoreElements()) {
                sb.append(System.lineSeparator());
                sb.append("Headers: ");
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
                sb.append("AuthType: ");
                sb.append(httpRequest.getAuthType());
            }
            //Cookies
            if (httpRequest.getCookies().length > 0) {
                sb.append(System.lineSeparator());
                sb.append("Cookies: ");
                for (Cookie cookie : httpRequest.getCookies()) {
                    sb.append(System.lineSeparator());
                    sb.append(cookie.getName());
                    sb.append(cookie.getValue());
                }
            }
            //RemoteAddr
            if (httpRequest.getRemoteAddr() != null && !httpRequest.getRemoteAddr().isEmpty()) {
                sb.append(System.lineSeparator());
                sb.append("RemoteAddr: ");
                sb.append(httpRequest.getRemoteAddr());
            }
            //RemoteHost
            if (httpRequest.getRemoteHost() != null && !httpRequest.getRemoteHost().isEmpty()) {
                sb.append(System.lineSeparator());
                sb.append("RemoteHost: ");
                sb.append(httpRequest.getRemoteHost());
            }
            //User principal
            if (httpRequest.getUserPrincipal() != null) {
                if (httpRequest.getUserPrincipal().getName() != null && !httpRequest.getUserPrincipal().getName().isEmpty()) {
                    sb.append(System.lineSeparator());
                    sb.append("Principal: ");
                    sb.append(httpRequest.getUserPrincipal().getName());
                }
            }
        }
        //Body
        String body = IOUtils.toString(wrappedRequest.getReader());
        if (body != null && !body.isEmpty())
        {
            sb.append(System.lineSeparator());
            sb.append("Body: ");
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
