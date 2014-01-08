/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.stitchgalaxy.sg_manager_web;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.GenericServlet;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Administrator
 */
public class ErrorHandler {
    private Exception exception;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private String message;
    private GenericServlet servlet;
    
    public void process() throws ServletException, IOException
    {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        getException().printStackTrace(pw);
        getRequest().setAttribute("stack_trace", sw.toString());
        getRequest().setAttribute("error_message", getMessage());
        
        RequestDispatcher rd = getServlet().getServletContext().getRequestDispatcher("/error.jsp");
        rd.forward(getRequest(), getResponse());
    }

    /**
     * @return the exception
     */
    public Exception getException() {
        return exception;
    }

    /**
     * @param exception the exception to set
     */
    public void setException(Exception exception) {
        this.exception = exception;
    }

    /**
     * @return the request
     */
    public HttpServletRequest getRequest() {
        return request;
    }

    /**
     * @param request the request to set
     */
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * @return the response
     */
    public HttpServletResponse getResponse() {
        return response;
    }

    /**
     * @param response the response to set
     */
    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the servlet
     */
    public GenericServlet getServlet() {
        return servlet;
    }

    /**
     * @param servlet the servlet to set
     */
    public void setServlet(GenericServlet servlet) {
        this.servlet = servlet;
    }
    
}
