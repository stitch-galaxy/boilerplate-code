/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.stitchgalaxy.sg_manager_web;

import java.io.IOException;
import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.WebServlet;

/**
 *
 * @author tarasev
 */
@WebServlet("/product-new")
public class ProductNewServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher rd = getServletContext().getRequestDispatcher("/product-new.jsp");
        rd.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String errorMessage = "Unable to cast product parameters";
        Long productId = 1l;
        try
        {
            String sName = request.getParameter("name");
            String sDate = request.getParameter("date");
            String sPriceUsd = request.getParameter("price");
            errorMessage = "Unable to store new product";
            //TODO: store new product.
        }
        catch(Exception e)
        {
            ErrorHandler errorHandler = new ErrorHandler();
            errorHandler.setException(e);
            errorHandler.setMessage(errorMessage);
            errorHandler.setRequest(request);
            errorHandler.setResponse(response);
            errorHandler.setServlet(this);
            errorHandler.process();
            return;
        }
        response.sendRedirect(String.format("%s%s?product=%d", request.getContextPath(), "/product-view", productId));
    }
}
