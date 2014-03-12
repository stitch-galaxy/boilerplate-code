/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stitchgalaxy.sg_manager_web;

import com.stitchgalaxy.service.DomainDataService;
import com.stitchgalaxy.dto.CategoryInfoDTO;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author tarasev
 */
@Controller
public class CategoriesController {

    @Autowired DomainDataService domainDataService;
    
    @RequestMapping(value = UrlConstants.URL_CATEGORY_VIEW, method = RequestMethod.GET)
    public String getCategory(Model model,
            @RequestParam(value = "category") Long categoryId) {
        CategoryInfoDTO category = domainDataService.getCategoryById(categoryId);
        model.addAttribute("category", category);
        UrlConstants.AddUrlConstants(model);
        
        return "category-view";
    }
    
    @RequestMapping(value = UrlConstants.URL_CATEGORY_ADD, method = RequestMethod.POST)
    public String addCategory(Model model,
            @RequestParam(value = "category") Long categoryId,
            @RequestParam(value = "name") String name,
            RedirectAttributes redirectAttributes) {
            domainDataService.createSubcategory(categoryId, name);
            redirectAttributes.addAttribute("category", categoryId);
            
            return "redirect:" + UrlConstants.URL_CATEGORY_VIEW;
    }
    
    @RequestMapping(value = UrlConstants.URL_CATEGORY_REMOVE, method = RequestMethod.GET)
    public String removeCategory(Model model,
            @RequestParam(value = "category") Long categoryId,
            @RequestParam(value = "sub-category") Long subCategoryId,
            RedirectAttributes redirectAttributes)
    {
        domainDataService.removeSubcategory(categoryId, subCategoryId);
        redirectAttributes.addAttribute("category", categoryId);
        
        return "redirect:" + UrlConstants.URL_CATEGORY_VIEW;
    }
    
    @RequestMapping(value = UrlConstants.URL_CATEGORY_VIEW_TOPLEVEL, method = RequestMethod.GET)
    public String listTopLevelCategories(Model model) {
        List<CategoryInfoDTO> categories = domainDataService.getRootCategories();
        model.addAttribute("categories", categories);
        UrlConstants.AddUrlConstants(model);
        
        return "category-view-toplevel";
    }
    
    @RequestMapping(value = UrlConstants.URL_CATEGORY_ADD_TOPLEVEL, method = RequestMethod.POST)
    public String addTopLevelCategory(Model model, 
            @RequestParam("name") String name)
    {
        domainDataService.createTopLevel—ategory(name);
        
        return "redirect:" + UrlConstants.URL_CATEGORY_VIEW_TOPLEVEL;
    }
    
    @RequestMapping(value = UrlConstants.URL_CATEGORY_REMOVE_TOPLEVEL, method = RequestMethod.GET)
    public String removeTopLevelCategory(Model model, 
            @RequestParam(value="category") Long categoryId) throws Exception
    {
        domainDataService.removeTopLevelCategory(categoryId);
        
        return "redirect:" + UrlConstants.URL_CATEGORY_VIEW_TOPLEVEL;
    }
}
