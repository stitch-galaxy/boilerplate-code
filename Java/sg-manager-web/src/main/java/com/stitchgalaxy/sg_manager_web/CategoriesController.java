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
import javax.jws.WebParam;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author tarasev
 */
@Controller
public class CategoriesController {

    @Autowired DomainDataService domainDataService;
    
    @RequestMapping(value = "/category/view/topLevel", method = RequestMethod.GET)
    public String listTopLevelCategories(Model model) {
        List<CategoryInfoDTO> categories = domainDataService.getRootCategories();
        model.addAttribute("categories", categories);

        return "categories-manage-toplevel";
    }
}
