/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.stitchgalaxy.sg_manager_web;

import com.stitchgalaxy.sg_manager_web.data.Design;
import com.stitchgalaxy.sg_manager_web.data.Product;
import java.awt.Color;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
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
import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;

/**
 *
 * @author tarasev
 */
@WebServlet("/product-design-edit")
public class ProductDesignEditServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String errorMessage = ErrorHandler.BAD_REQUEST_PARAMETERS;
        Design design = null;
        try
        {
            String sProductId = request.getParameter("product");
            Long productId = Long.parseLong(sProductId);
            String sDesignId = request.getParameter("design");
            Long designId = Long.parseLong(sDesignId);
            errorMessage = "Can not load design data";
            design = TestData.createProductDesign();
            //TODO: fetch product
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
        request.setAttribute("design", design);
        RequestDispatcher rd = getServletContext().getRequestDispatcher("/product-design-edit.jsp");
        rd.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String errorMessage = ErrorHandler.BAD_REQUEST_PARAMETERS;
        Long productId = null;
        try
        {
            String sProductId = request.getParameter("product");
            productId = Long.parseLong(sProductId);
            String sDesignId = request.getParameter("design");
            Long designId = Long.parseLong(sDesignId);
            
            
            String sWidth = request.getParameter("width");
            Integer width = StringUtils.isNotEmpty(sWidth) ? Integer.parseInt(sWidth) : null;
            String sHeight = request.getParameter("height");
            Integer height = StringUtils.isNotEmpty(sHeight) ? Integer.parseInt(sHeight) : null;
            String sColors = request.getParameter("colors");
            Integer colors = StringUtils.isNotEmpty(sColors) ? Integer.parseInt(sColors) : null;
            
            
            String canvas = request.getParameter("canvas");
            
            String sStitchesPerInch = request.getParameter("stitchesPerInch");
            BigDecimal stitchesPerInch = StringUtils.isNotEmpty(sStitchesPerInch) ? new BigDecimal(sStitchesPerInch) : null;
            
            String threads = request.getParameter("threads");
            
            errorMessage = "Unable to store design";
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
