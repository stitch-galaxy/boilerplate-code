/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stitchgalaxy.sg_manager_web;

import com.stitchgalaxy.service.DomainDataService;
import com.stitchgalaxy.dto.CategoryInfoDTO;
import com.stitchgalaxy.dto.CommandCreateSubcategory;
import com.stitchgalaxy.dto.CommandCreateTopLevelCategory;
import com.stitchgalaxy.dto.CommandGetCategory;
import com.stitchgalaxy.dto.CommandGetRootCategories;
import com.stitchgalaxy.dto.CommandRemoveSubcategory;
import com.stitchgalaxy.dto.CommandRemoveTopLevelCategory;
import com.stitchgalaxy.service.DomainDataServiceException;
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

    @Autowired
    DomainDataService domainDataService;

    @RequestMapping(value = UrlConstants.URL_CATEGORY_VIEW, method = RequestMethod.GET)
    public String getCategory(Model model,
            @RequestParam(value = "category") Long categoryId) throws DomainDataServiceException {
        CommandGetCategory command = new CommandGetCategory();
        command.setCategoryId(categoryId);
        CategoryInfoDTO category = domainDataService.getCategoryById(command);
        model.addAttribute("category", category);
        UrlConstants.AddUrlConstants(model);

        return "category-view";
    }

    @RequestMapping(value = UrlConstants.URL_CATEGORY_ADD, method = RequestMethod.POST)
    public String addCategory(Model model,
            @RequestParam(value = "category") Long categoryId,
            @RequestParam(value = "name") String name,
            RedirectAttributes redirectAttributes) throws DomainDataServiceException {

        CommandCreateSubcategory command = new CommandCreateSubcategory();
        command.setCategoryId(categoryId);
        command.setName(name);
        domainDataService.createSubcategory(command);
        redirectAttributes.addAttribute("category", categoryId);

        return "redirect:" + UrlConstants.URL_CATEGORY_VIEW;
    }

    @RequestMapping(value = UrlConstants.URL_CATEGORY_REMOVE, method = RequestMethod.GET)
    public String removeCategory(Model model,
            @RequestParam(value = "category") Long categoryId,
            @RequestParam(value = "sub-category") Long subCategoryId,
            RedirectAttributes redirectAttributes) throws DomainDataServiceException {
        CommandRemoveSubcategory command = new CommandRemoveSubcategory();
        command.setCategoryId(categoryId);
        command.setSubCategoryId(subCategoryId);
        domainDataService.removeSubcategory(command);
        redirectAttributes.addAttribute("category", categoryId);

        return "redirect:" + UrlConstants.URL_CATEGORY_VIEW;
    }

    @RequestMapping(value = UrlConstants.URL_CATEGORY_VIEW_TOPLEVEL, method = RequestMethod.GET)
    public String listTopLevelCategories(Model model) {
        List<CategoryInfoDTO> categories = domainDataService.getRootCategories(new CommandGetRootCategories());
        model.addAttribute("categories", categories);
        UrlConstants.AddUrlConstants(model);

        return "category-view-toplevel";
    }

    @RequestMapping(value = UrlConstants.URL_CATEGORY_ADD_TOPLEVEL, method = RequestMethod.POST)
    public String addTopLevelCategory(Model model,
            @RequestParam("name") String name) {
        CommandCreateTopLevelCategory command = new CommandCreateTopLevelCategory();
        command.setName(name);
        domainDataService.createTopLevel—ategory(command);

        return "redirect:" + UrlConstants.URL_CATEGORY_VIEW_TOPLEVEL;
    }

    @RequestMapping(value = UrlConstants.URL_CATEGORY_REMOVE_TOPLEVEL, method = RequestMethod.GET)
    public String removeTopLevelCategory(Model model,
            @RequestParam(value = "category") Long categoryId) throws Exception {
        CommandRemoveTopLevelCategory command = new CommandRemoveTopLevelCategory();
        command.setCategoryId(categoryId);
        domainDataService.removeTopLevelCategory(command);

        return "redirect:" + UrlConstants.URL_CATEGORY_VIEW_TOPLEVEL;
    }
}
