/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stitchgalaxy.sg_manager_web;

import com.stitchgalaxy.dao.DomainDataService;
import com.stitchgalaxy.domain.Design;
import java.io.IOException;
import java.math.BigDecimal;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.WebServlet;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author tarasev
 */
@WebServlet("/product-design-edit")
public class ProductDesignEditServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Long productId = Long.parseLong(request.getParameter("product"));
            Long designId = Long.parseLong(request.getParameter("design"));
            Design design = DomainDataService.getInstance().getDesignById(designId);
            request.setAttribute("design", design);
            request.setAttribute("productId", productId);
        } catch (Exception e) {
            ErrorHandler errorHandler = new ErrorHandler(e, request, response, this);
            errorHandler.process();
            return;
        }

        RequestDispatcher rd = getServletContext().getRequestDispatcher("/product-design-edit.jsp");
        rd.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Long designId = Long.parseLong(request.getParameter("design"));
            Design design = DomainDataService.getInstance().getDesignById(designId);

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

            design.setWidth(width);
            design.setHeight(height);
            design.setColors(colors);
            design.setCanvas(canvas);
            design.setStitchesPerInch(stitchesPerInch);
            design.setThreads(threads);

            DomainDataService.getInstance().storeDesignData(design);
        } catch (Exception e) {
            ErrorHandler errorHandler = new ErrorHandler(e, request, response, this);
            errorHandler.process();
            return;
        }
        response.sendRedirect(String.format("%s%s?product=%s", request.getContextPath(), "/product-view", request.getParameter("product")));
    }
}
