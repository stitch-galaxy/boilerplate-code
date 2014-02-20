/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stitchgalaxy.sg_manager_web;

import com.stitchgalaxy.service.DomainDataService;
import com.stitchgalaxy.dto.CategoryInfoDTO;
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
@WebServlet("/categories-manage-toplevel")
public class CategoriesManageTopLevelServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<CategoryInfoDTO> categories = DomainDataServiceUtils.getDomainDataService(this).getRootCategories();
            request.setAttribute("categories", categories);
        } catch (Exception e) {
            ErrorHandler errorHandler = new ErrorHandler(e, request, response, this);
            errorHandler.process();
            return;
        }

        RequestDispatcher rd = getServletContext().getRequestDispatcher("/categories-manage-toplevel.jsp");
        rd.forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String name = request.getParameter("name");
            DomainDataServiceUtils.getDomainDataService(this).createTopLevel—ategory(name);
        } catch (NumberFormatException e) {
            ErrorHandler errorHandler = new ErrorHandler(e, request, response, this);
            errorHandler.process();
            return;
        }
        doGet(request, response);
    }
}
