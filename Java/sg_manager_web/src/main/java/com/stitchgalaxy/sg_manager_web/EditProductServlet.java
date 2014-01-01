/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.stitchgalaxy.sg_manager_web;

import com.stitchgalaxy.sg_manager_web.data.Product;
import com.stitchgalaxy.sg_manager_web.data.ProductRef;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author tarasev
 */
public class EditProductServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Product product = null;
        //TODO: fetch product
        product = new Product();
        request.setAttribute("product", product);
        
        RequestDispatcher rd = getServletContext().getRequestDispatcher("/edit_product.jsp");
        rd.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String sId = request.getParameter("id");
        String sName = request.getParameter("name");
        String sDate = request.getParameter("date");
        String sPriceUsd = request.getParameter("price");
        try
        {
            //TODO: store product.
            response.sendRedirect("/");
        }
        catch(Exception e)
        {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            request.setAttribute("error_message", sw.toString());

            RequestDispatcher rd = getServletContext().getRequestDispatcher("/error_save_product.jsp");
            rd.forward(request, response);
        }
    }


    @Override
    public String getServletInfo() {
        return "SG manager edit product servlet";
    }

}
