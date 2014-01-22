/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.stitchgalaxy.sg_manager_web;

import com.stitchgalaxy.domain.Product;
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
import org.joda.time.LocalDate;

/**
 *
 * @author tarasev
 */
@WebServlet("/product-edit")
public class ProductEditServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String errorMessage = ErrorHandler.BAD_REQUEST_PARAMETERS;
        Product product = null;
        try
        {
            String sProductId = request.getParameter("product");
            Long productId = Long.parseLong(sProductId);
            errorMessage = "Can not load product data";
            product = TestData.createProductData();
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
        request.setAttribute("product", product);
        RequestDispatcher rd = getServletContext().getRequestDispatcher("/product-edit.jsp");
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
            String name = request.getParameter("name");
            String sDate = request.getParameter("date");
            LocalDate date = LocalDate.parse(sDate);
            String sPriceUsd = request.getParameter("price");
            BigDecimal priceUsd = new BigDecimal(sPriceUsd);
            String sBlocked = request.getParameter("blocked");
            Boolean blocked = Boolean.FALSE;
            if (sBlocked.equals("on") || sBlocked.equals("yes") || sBlocked.equals("checked") || sBlocked.equals("true"))
            {
                blocked = Boolean.TRUE;
            }
            String description = request.getParameter("description");
            String sSales = request.getParameter("sales");
            Long sales = Long.parseLong(sSales);
            String sRating = request.getParameter("rating");
            Long rating = Long.parseLong(sRating);
            String sRates = request.getParameter("rates");
            Long rates = Long.parseLong(sRates);
            String sComplexity = request.getParameter("complexity");
            Integer complextity = Integer.parseInt(sComplexity);
            String tags = request.getParameter("tags");
            String sColor = request.getParameter("color");
            Color color = Color.decode(sColor.replace("#", "0x").toLowerCase());
            errorMessage = "Unable to store product";
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
