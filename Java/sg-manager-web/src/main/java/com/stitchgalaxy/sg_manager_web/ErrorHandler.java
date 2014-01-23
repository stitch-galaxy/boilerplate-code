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
public final class ErrorHandler {

    private final Exception exception;
    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final GenericServlet servlet;

    public static final String BAD_REQUEST_PARAMETERS = "Bad request parameters";

    public ErrorHandler(Exception exception, HttpServletRequest request, HttpServletResponse response, GenericServlet servlet) {
        this.exception = exception;
        this.request = request;
        this.response = response;
        this.servlet = servlet;
    }

    public void process() throws ServletException, IOException {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        request.setAttribute("stack_trace", sw.toString());

        RequestDispatcher rd = servlet.getServletContext().getRequestDispatcher("/error.jsp");
        rd.forward(request, response);
    }
}
