/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.stitchgalaxy.sg_manager_web;

import com.stitchgalaxy.sg_manager_web.data.Category;
import com.stitchgalaxy.sg_manager_web.data.ProductLocalization;
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
import org.joda.time.LocalDate;

/**
 *
 * @author tarasev
 */
@WebServlet("/product-category-select")
public class ProductCategorySelectServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String errorMessage = ErrorHandler.BAD_REQUEST_PARAMETERS;
        
        try
        {
            Long categoryId = Long.parseLong(request.getParameter("category"));
            Long productId = Long.parseLong(request.getParameter("product"));
            errorMessage = "Cannot load category";
            Category category = TestData.createProductCategory();
            //TODO: load product category
            request.setAttribute("category", category);
            request.setAttribute("productId", productId);
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
        
        RequestDispatcher rd = getServletContext().getRequestDispatcher("/product-category-select.jsp");
        rd.forward(request, response);
    }
}
