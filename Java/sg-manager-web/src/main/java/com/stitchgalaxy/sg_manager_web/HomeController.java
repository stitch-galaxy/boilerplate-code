/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stitchgalaxy.sg_manager_web;

import com.stitchgalaxy.dto.CommandGetProducts;
import com.stitchgalaxy.dto.ProductInfo;
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

    @RequestMapping(value = {"", "/", UrlConstants.URL_HOME}, method = RequestMethod.GET)
    public String home(ModelMap model) {

        List<ProductInfo> products = domainDataService.getAllProducts(new CommandGetProducts());
        model.addAttribute("products", products);
        UrlConstants.AddUrlConstants(model);
        
        return "home";
    }

}
