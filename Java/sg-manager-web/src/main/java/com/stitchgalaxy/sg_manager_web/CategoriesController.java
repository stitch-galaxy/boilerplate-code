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

/**
 *
 * @author tarasev
 */
@Controller
public class CategoriesController {

    @Autowired DomainDataService domainDataService;
    
    public static final String VIEW_TOPLEVEL_URL = "/category/view/topLevel";
    public static final String ADD_TOPLEVEL_URL = "/category/add/topLevel";
    public static final String REMOVE_TOPLEVEL_URL = "/category/remove/topLevel";
    
    @RequestMapping(value = VIEW_TOPLEVEL_URL, method = RequestMethod.GET)
    public String listTopLevelCategories(Model model) {
        List<CategoryInfoDTO> categories = domainDataService.getRootCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("postAction", ADD_TOPLEVEL_URL);
        model.addAttribute("removeAction", REMOVE_TOPLEVEL_URL);

        return "category-view-toplevel";
    }
    
    @RequestMapping(value = ADD_TOPLEVEL_URL, method = RequestMethod.POST)
    public String addTopLevelCategory(Model model, 
            @RequestParam("name") String name)
    {
        domainDataService.createTopLevel—ategory(name);
        return "redirect:" + VIEW_TOPLEVEL_URL;
    }
    
    @RequestMapping(value = REMOVE_TOPLEVEL_URL, method = RequestMethod.GET)
    public String removeTopLevelCategory(Model model, 
            @RequestParam(value="category") Long categoryId)
    {
        domainDataService.removeTopLevelCategory(categoryId);
        return "redirect:" + VIEW_TOPLEVEL_URL;
    }
}
