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
@WebServlet("/design-remove-file")
public class ProductDesignRemoveFileServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String errorMessage = ErrorHandler.BAD_REQUEST_PARAMETERS;
        Long productId = null;
        Long designId = null;
        try
        {
            String sProductId = request.getParameter("product");
            productId = Long.parseLong(sProductId);
            String sDesignId = request.getParameter("design");
            designId = Long.parseLong(sDesignId);
            //TODO: remove file
            errorMessage = "Unable to remove thumbnail";
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
        
        response.sendRedirect(String.format("%s%s?product=%d&design=%d", request.getContextPath(), "/product-design-edit", productId, designId));

    }
}
