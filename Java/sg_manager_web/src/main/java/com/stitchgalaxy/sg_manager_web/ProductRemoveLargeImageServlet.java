/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.stitchgalaxy.sg_manager_web;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author tarasev
 */
@WebServlet("/product-remove-large-image")
public class ProductRemoveLargeImageServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String errorMessage = ErrorHandler.BAD_REQUEST_PARAMETERS;
        Long productId = null;
        try
        {
            String sProductId = request.getParameter("product");
            productId = Long.parseLong(sProductId);
            //TODO: remove file
            errorMessage = "Unable to remove image";
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
