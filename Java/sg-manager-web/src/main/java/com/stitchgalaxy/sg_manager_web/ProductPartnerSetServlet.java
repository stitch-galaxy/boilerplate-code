/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stitchgalaxy.sg_manager_web;

import com.stitchgalaxy.service.DomainDataService;
import com.stitchgalaxy.domain.Partner;
import com.stitchgalaxy.dto.CommandCreatePartner;
import com.stitchgalaxy.dto.CommandGetPartners;
import com.stitchgalaxy.dto.PartnerInfo;
import java.io.IOException;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author tarasev
 */
@WebServlet(urlPatterns = {"/product-set-author", "/product-set-translator"})
public class ProductPartnerSetServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            if (request.getServletPath().equals("/product-set-author")) {
                request.setAttribute("action", "/product-assign-author");
            } else if (request.getServletPath().equals("/product-set-translator")) {
                request.setAttribute("action", "/product-assign-translator");
            }
            List<PartnerInfo> partners = DomainDataServiceUtils.getDomainDataService(this).getAllPartners(new CommandGetPartners());
            request.setAttribute("partners", partners);
        } catch (Exception e) {
            ErrorHandler errorHandler = new ErrorHandler(e, request, response, this);
            errorHandler.process();
            return;
        }

        RequestDispatcher rd = getServletContext().getRequestDispatcher("/partners.jsp");
        rd.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String name = request.getParameter("name");
            String uri = request.getParameter("uri");
            CommandCreatePartner command = new CommandCreatePartner();
            command.setName(name);
            command.setUri(uri);
            DomainDataServiceUtils.getDomainDataService(this).addPartner(command);
        } catch (Exception e) {
            ErrorHandler errorHandler = new ErrorHandler(e, request, response, this);
            errorHandler.process();
            return;
        }
        doGet(request, response);
    }
}
