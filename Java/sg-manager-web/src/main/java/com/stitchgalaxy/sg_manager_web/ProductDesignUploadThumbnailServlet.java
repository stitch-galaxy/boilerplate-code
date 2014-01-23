/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stitchgalaxy.sg_manager_web;

import com.stitchgalaxy.dao.DomainDataService;
import java.io.IOException;
import java.io.InputStream;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 *
 * @author tarasev
 */
@WebServlet("/design-upload-thumbnail")
@MultipartConfig
public class ProductDesignUploadThumbnailServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Long designId = Long.parseLong(request.getParameter("design"));
            Part filePart = request.getPart("file");
            InputStream filecontent = filePart.getInputStream();
            DomainDataService.getInstance().uploadDesignThumbnail(designId, filecontent);
        } catch (Exception e) {
            ErrorHandler errorHandler = new ErrorHandler(e, request, response, this);
            errorHandler.process();
            return;
        }
        response.sendRedirect(String.format("%s%s?product=%s&design=%s", request.getContextPath(), "/product-design-edit", request.getParameter("product"), request.getParameter("design")));
    }
}
