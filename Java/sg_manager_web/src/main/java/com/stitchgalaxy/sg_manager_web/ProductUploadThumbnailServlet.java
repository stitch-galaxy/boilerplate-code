/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.stitchgalaxy.sg_manager_web;

import com.stitchgalaxy.domain.Partner;
import com.stitchgalaxy.domain.Product;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author tarasev
 */
@WebServlet("/product-upload-thumbnail")
@MultipartConfig
public class ProductUploadThumbnailServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String errorMessage = ErrorHandler.BAD_REQUEST_PARAMETERS;
        Long productId = null;
        try
        {
            String sProductId = request.getParameter("product");
            productId = Long.parseLong(sProductId);
            Part filePart = request.getPart("file");
            InputStream filecontent = filePart.getInputStream();
            //TODO: store file
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
