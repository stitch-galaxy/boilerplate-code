/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.stitchgalaxy.sg_manager_web;

import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;

/**
 *
 * @author tarasev
 */
public class UrlConstants {
    
    public static final String URL_CATEGORY_VIEW_TOPLEVEL = "/category/view/topLevel";
    public static final String URL_CATEGORY_ADD_TOPLEVEL = "/category/add/topLevel";
    public static final String URL_CATEGORY_REMOVE_TOPLEVEL = "/category/remove/topLevel";
    public static final String URL_CATEGORY_VIEW = "/category/view";
    public static final String URL_CATEGORY_ADD = "/category/add";
    public static final String URL_CATEGORY_REMOVE = "/category/remove";
    
    public static final String URL_HOME = "/home";
    
    public static final String URL_PRODUCT_VIEW = "/product/view";
    public static final String URL_PRODUCT_ADD = "/product/add";
    public static final String URL_PRODUCT_EDIT = "/product/edit";
    public static final String URL_PRODUCT_REMOVE = "/product/remove";
    
    public static final String URL_PRODUCT_SELECT_AUTHOR = "/product/author/select";
    public static final String URL_PRODUCT_SELECT_TRANSLATOR = "/product/translator/select";
    public static final String URL_PRODUCT_REMOVE_AUTHOR = "/product/author/remove";
    public static final String URL_PRODUCT_REMOVE_TRANSLATOR = "/product/translator/remove";
    public static final String URL_PRODUCT_ASSIGN_AUTHOR = "/product/author/assign";
    public static final String URL_PRODUCT_ASSIGN_TRANSLATOR = "/product/translator/assign";
    
    public static final String URL_LOGIN = "/login";
    public static final String URL_LOGIN_FAILED = "/loginfailed";
    public static final String URL_LOGOUT = "/logout";
    
    public static void AddUrlConstants(ModelWrapper model)
    {
        model.addAttribute("URL_CATEGORY_VIEW_TOPLEVEL", URL_CATEGORY_VIEW_TOPLEVEL);
        model.addAttribute("URL_CATEGORY_ADD_TOPLEVEL", URL_CATEGORY_ADD_TOPLEVEL);
        model.addAttribute("URL_CATEGORY_REMOVE_TOPLEVEL", URL_CATEGORY_REMOVE_TOPLEVEL);
        model.addAttribute("URL_CATEGORY_VIEW", URL_CATEGORY_VIEW);
        model.addAttribute("URL_CATEGORY_ADD", URL_CATEGORY_ADD);
        model.addAttribute("URL_CATEGORY_REMOVE", URL_CATEGORY_REMOVE);
        model.addAttribute("URL_HOME", URL_HOME);
        model.addAttribute("URL_PRODUCT_VIEW", URL_PRODUCT_VIEW);
        model.addAttribute("URL_PRODUCT_ADD", URL_PRODUCT_ADD);
        model.addAttribute("URL_PRODUCT_EDIT", URL_PRODUCT_EDIT);
        model.addAttribute("URL_PRODUCT_REMOVE", URL_PRODUCT_REMOVE);
        
        model.addAttribute("URL_PRODUCT_SELECT_AUTHOR", URL_PRODUCT_SELECT_AUTHOR);
        model.addAttribute("URL_PRODUCT_SELECT_TRANSLATOR", URL_PRODUCT_SELECT_TRANSLATOR);
        model.addAttribute("URL_PRODUCT_REMOVE_AUTHOR", URL_PRODUCT_REMOVE_AUTHOR);
        model.addAttribute("URL_PRODUCT_REMOVE_TRANSLATOR", URL_PRODUCT_REMOVE_TRANSLATOR);
        model.addAttribute("URL_PRODUCT_ASSIGN_AUTHOR", URL_PRODUCT_ASSIGN_AUTHOR);
        model.addAttribute("URL_PRODUCT_ASSIGN_TRANSLATOR", URL_PRODUCT_ASSIGN_TRANSLATOR);
    }
    
    public static class ModelWrapper
    {
        private Model model = null;
        private ModelMap modelMap = null;
        public ModelWrapper(Model model)
        {
            this.model = model;
        }
        
        public ModelWrapper(ModelMap modelMap)
        {
            this.modelMap = modelMap;
        }
        
        public void addAttribute(String name, String value)
        {
            if (model != null)
            {
                model.addAttribute(name, value);
            }
            if (modelMap != null)
            {
                modelMap.addAttribute(name, value);
            }
        }
    }
    
    public static void AddUrlConstants(Model model)
    {
        ModelWrapper wrapper = new ModelWrapper(model);
        AddUrlConstants(wrapper);
    }
    
    public static void AddUrlConstants(ModelMap model)
    {
        ModelWrapper wrapper = new ModelWrapper(model);
        AddUrlConstants(wrapper);
    }
}
