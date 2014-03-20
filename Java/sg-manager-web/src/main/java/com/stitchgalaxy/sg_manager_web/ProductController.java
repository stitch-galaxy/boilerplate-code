/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stitchgalaxy.sg_manager_web;

import com.stitchgalaxy.dto.CommandCreateProduct;
import com.stitchgalaxy.dto.CommandGetPartners;
import com.stitchgalaxy.dto.CommandGetProduct;
import com.stitchgalaxy.dto.PartnerInfo;
import com.stitchgalaxy.dto.ProductInfo;
import com.stitchgalaxy.service.DomainDataService;
import com.stitchgalaxy.service.DomainDataServiceException;
import java.math.BigDecimal;
import java.util.List;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author tarasev
 */
@Controller
public class ProductController {

    @Autowired
    DomainDataService domainDataService;

    @RequestMapping(value = UrlConstants.URL_PRODUCT_ADD, method = RequestMethod.POST)
    public String addProductCommand(Model model,
            @RequestParam(value = "name") String name,
            @RequestParam(value = "date") String sDate,
            @RequestParam(value = "price") BigDecimal price) throws DomainDataServiceException {
        CommandCreateProduct command = new CommandCreateProduct();
        LocalDate date = LocalDate.parse(sDate);
        command.setDate(date);
        command.setName(name);
        command.setPrice(price);
        domainDataService.createNewProduct(command);
        return "redirect:" + UrlConstants.URL_HOME;
    }

    @RequestMapping(value = UrlConstants.URL_PRODUCT_ADD, method = RequestMethod.GET)
    public String addProduct(Model model) throws DomainDataServiceException {
        UrlConstants.AddUrlConstants(model);
        return "product-new";
    }
    
    @RequestMapping(value = UrlConstants.URL_PRODUCT_VIEW, method = RequestMethod.GET)
    public String getProduct(Model model,
            @RequestParam(value = "product") Long productId) throws DomainDataServiceException {
        CommandGetProduct command = new CommandGetProduct();
        command.setProductId(productId);
        ProductInfo info = domainDataService.getProductById(command);
        model.addAttribute("product", info);
        UrlConstants.AddUrlConstants(model);
        return "product-view";
    }
    
    @RequestMapping(value = UrlConstants.URL_PRODUCT_SELECT_AUTHOR, method = RequestMethod.GET)
    public String selectAuthor(Model model,
            @RequestParam(value = "product") Long productId) throws DomainDataServiceException {
        List<PartnerInfo> partners = domainDataService.getAllPartners(new CommandGetPartners());
        model.addAttribute("partners", partners);
        model.addAttribute("action", UrlConstants.URL_PRODUCT_ASSIGN_AUTHOR);
        UrlConstants.AddUrlConstants(model);
        return "partners";
    }
    
    @RequestMapping(value = UrlConstants.URL_PRODUCT_SELECT_TRANSLATOR, method = RequestMethod.GET)
    public String selectTranslator(Model model,
            @RequestParam(value = "product") Long productId) throws DomainDataServiceException {
        List<PartnerInfo> partners = domainDataService.getAllPartners(new CommandGetPartners());
        model.addAttribute("partners", partners);
        model.addAttribute("action", UrlConstants.URL_PRODUCT_ASSIGN_TRANSLATOR);
        UrlConstants.AddUrlConstants(model);
        return "partners";
    }
    
    @RequestMapping(value = UrlConstants.URL_PRODUCT_SELECT_AUTHOR, method = RequestMethod.POST)
    public String addAuthor(Model model,
            @RequestParam(value = "product") Long productId) throws DomainDataServiceException {
        List<PartnerInfo> partners = domainDataService.getAllPartners(new CommandGetPartners());
        model.addAttribute("partners", partners);
        model.addAttribute("action", UrlConstants.URL_PRODUCT_ASSIGN_AUTHOR);
        UrlConstants.AddUrlConstants(model);
        return "partners";
    }

}