/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stitchgalaxy.sg_manager_web;

import com.stitchgalaxy.service.DomainDataService;
import com.stitchgalaxy.domain.Product;
import java.awt.Color;
import java.io.IOException;
import java.math.BigDecimal;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
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
        try {
            Long productId = Long.parseLong(request.getParameter("product"));
            Product product = DomainDataService.getInstance().getProductById(productId);
            request.setAttribute("product", product);
        } catch (Exception e) {
            ErrorHandler errorHandler = new ErrorHandler(e, request, response, this);
            errorHandler.process();
            return;
        }

        RequestDispatcher rd = getServletContext().getRequestDispatcher("/product-edit.jsp");
        rd.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Long productId = Long.parseLong(request.getParameter("product"));
            Product product = DomainDataService.getInstance().getProductById(productId);

            String name = request.getParameter("name");
            String sDate = request.getParameter("date");
            LocalDate date = LocalDate.parse(sDate);
            String sPriceUsd = request.getParameter("price");
            BigDecimal priceUsd = new BigDecimal(sPriceUsd);
            String sBlocked = request.getParameter("blocked");
            Boolean blocked = Boolean.FALSE;
            if (sBlocked.equals("on") || sBlocked.equals("yes") || sBlocked.equals("checked") || sBlocked.equals("true")) {
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

            product.setName(name);
            product.setDate(date);
            product.setPriceUsd(priceUsd);
            product.setBlocked(blocked);
            product.setDescription(description);
            product.setSales(sales);
            product.setRates(rates);
            product.setRating(rating);
            product.setComplexity(complextity);
            product.setTags(tags);
            product.setAvgColor(color);

            DomainDataService.getInstance().storeProductData(product);
        } catch (Exception e) {
            ErrorHandler errorHandler = new ErrorHandler(e, request, response, this);
            errorHandler.process();
            return;
        }
        response.sendRedirect(String.format("%s%s?product=%s", request.getContextPath(), "/product-view", request.getParameter("product")));
    }
}
