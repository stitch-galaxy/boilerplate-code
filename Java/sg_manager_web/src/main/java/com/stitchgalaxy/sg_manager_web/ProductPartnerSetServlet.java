/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.stitchgalaxy.sg_manager_web;

import com.stitchgalaxy.sg_manager_web.data.Partner;
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
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author tarasev
 */
@WebServlet(urlPatterns = {"/product-set-author", "/product-set-translator"})
@MultipartConfig
public class ProductPartnerSetServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String errorMessage = ErrorHandler.BAD_REQUEST_PARAMETERS;
        
        try
        {
            if (request.getServletPath().equals("/product-set-author"))
            {
                request.setAttribute("action", "/product-assign-author");
            }
            else if (request.getServletPath().equals("/product-set-translator"))
            {
                request.setAttribute("action", "/product-assign-translator");
            }
            errorMessage = "Unable to load partners";
            //TODO: get all partners
            List<Partner> partners = TestData.createPartnersList();
            request.setAttribute("partners", partners);
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
        
        RequestDispatcher rd = getServletContext().getRequestDispatcher("/partners.jsp");
        rd.forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name");
        String uri = request.getParameter("uri");
        try
        {
            //TODO: save new author
        }
        catch(Exception e)
        {
            ErrorHandler errorHandler = new ErrorHandler();
            errorHandler.setException(e);
            errorHandler.setMessage("Unable to save partner");
            errorHandler.setRequest(request);
            errorHandler.setResponse(response);
            errorHandler.setServlet(this);
            errorHandler.process();
            return;
        }
        doGet(request, response);
    }
}
