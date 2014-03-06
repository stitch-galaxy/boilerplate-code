/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stitchgalaxy.sg_manager_web;

import com.stitchgalaxy.domain.Product;
import com.stitchgalaxy.service.DomainDataService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController {

    @Autowired
    DomainDataService domainDataService;

    @RequestMapping(value = {"", "/", "/home"}, method = RequestMethod.GET)
    public String home(ModelMap model) {

        List<Product> products = domainDataService.getAllProducts();
        model.addAttribute("products", products);
        model.addAttribute("categoryViewTopLevelAction", CategoriesController.VIEW_TOPLEVEL_URL); 
        return "home";
    }

}
