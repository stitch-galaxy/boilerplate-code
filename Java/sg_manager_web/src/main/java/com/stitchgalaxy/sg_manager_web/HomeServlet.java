/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.stitchgalaxy.sg_manager_web;

import com.stitchgalaxy.sg_manager_web.data.Product;
import java.io.IOException;
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

/**
 *
 * @author tarasev
 */
@WebServlet("")
@MultipartConfig
public class HomeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //TODO: populate products
        try
        {
            List<Product> products = new LinkedList<Product>();
            Product product = new Product();
            product.setName("Lovely owl");
            product.setId(1L);
            products.add(product);

            Product p2 = new Product();
            p2.setName("Pinguin");
            p2.setId(2l);
            products.add(p2);
            
            request.setAttribute("products", products);
        }
        catch(Exception e)
        {
            ErrorHandler errorHandler = new ErrorHandler();
            errorHandler.setException(e);
            errorHandler.setMessage("Unable to load products");
            errorHandler.setRequest(request);
            errorHandler.setResponse(response);
            errorHandler.setServlet(this);
            errorHandler.process();
            return;
        }        
        
        RequestDispatcher rd = getServletContext().getRequestDispatcher("/home.jsp");
        rd.forward(request, response);
    }
}