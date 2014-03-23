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
    
    public static final String URL_CATEGORY_VIEW_ROOT = "/category/view/root";
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
    
    
    public static final String URL_PRODUCT_SELECT_CATEGORY = "/product/category/set";
    public static final String URL_PRODUCT_ATTACH_CATEGORY = "/product/category/attach";
    public static final String URL_PRODUCT_DETACH_CATEGORY = "/product/category/detach";
   
    public static final String URL_PRODUCT_LOCALIZATION_REMOVE = "/product/localization/remove";
    public static final String URL_PRODUCT_LOCALIZATION_NEW = "/product/localization/new";
    public static final String URL_PRODUCT_LOCALIZATION_EDIT = "/product/localization/edit";
    
    public static final String URL_LOGIN = "/login";
    public static final String URL_LOGIN_FAILED = "/loginfailed";
    public static final String URL_LOGOUT = "/logout";
    
    
    public static final String URL_PRODUCT_UPLOAD_THUMBNAIL = "/product/thumbnail/upload";
    public static final String URL_PRODUCT_REMOVE_THUMBNAIL = "/product/thumbnail/remove";
    public static final String URL_PRODUCT_UPLOAD_IMAGE = "/product/image/upload";
    public static final String URL_PRODUCT_REMOVE_IMAGE = "/product/image/remove";
    public static final String URL_PRODUCT_UPLOAD_COMPLETE_PRODUCT = "/product/completeProduct/upload";
    public static final String URL_PRODUCT_REMOVE_COMPLETE_PRODUCT = "/product/completeProduct/remove";
    public static final String URL_PRODUCT_UPLOAD_PROTOTYPE = "/product/prototype/upload";
    public static final String URL_PRODUCT_REMOVE_PROTOTYPE = "/product/prototype/remove";
    public static final String URL_PRODUCT_UPLOAD_DESIGN = "/product/design/upload";
    public static final String URL_PRODUCT_REMOVE_DESIGN = "/product/design/remove";
    
    
    public static void AddUrlConstants(ModelWrapper model)
    {
        model.addAttribute("URL_CATEGORY_VIEW_TOPLEVEL", URL_CATEGORY_VIEW_ROOT);
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
        
        model.addAttribute("URL_PRODUCT_SELECT_CATEGORY", URL_PRODUCT_SELECT_CATEGORY);
        model.addAttribute("URL_PRODUCT_ATTACH_CATEGORY", URL_PRODUCT_ATTACH_CATEGORY);
        model.addAttribute("URL_PRODUCT_DETACH_CATEGORY", URL_PRODUCT_DETACH_CATEGORY);
        
        model.addAttribute("URL_PRODUCT_LOCALIZATION_REMOVE", URL_PRODUCT_LOCALIZATION_REMOVE);
        model.addAttribute("URL_PRODUCT_LOCALIZATION_NEW", URL_PRODUCT_LOCALIZATION_NEW);
        model.addAttribute("URL_PRODUCT_LOCALIZATION_EDIT", URL_PRODUCT_LOCALIZATION_EDIT);
        
        model.addAttribute("URL_PRODUCT_UPLOAD_THUMBNAIL", URL_PRODUCT_UPLOAD_THUMBNAIL);
        model.addAttribute("URL_PRODUCT_REMOVE_THUMBNAIL", URL_PRODUCT_REMOVE_THUMBNAIL);
        model.addAttribute("URL_PRODUCT_UPLOAD_IMAGE", URL_PRODUCT_UPLOAD_IMAGE);
        model.addAttribute("URL_PRODUCT_REMOVE_IMAGE", URL_PRODUCT_REMOVE_IMAGE);
        model.addAttribute("URL_PRODUCT_UPLOAD_COMPLETE_PRODUCT", URL_PRODUCT_UPLOAD_COMPLETE_PRODUCT);
        model.addAttribute("URL_PRODUCT_REMOVE_COMPLETE_PRODUCT", URL_PRODUCT_REMOVE_COMPLETE_PRODUCT);
        model.addAttribute("URL_PRODUCT_UPLOAD_PROTOTYPE", URL_PRODUCT_UPLOAD_PROTOTYPE);
        model.addAttribute("URL_PRODUCT_REMOVE_PROTOTYPE", URL_PRODUCT_REMOVE_PROTOTYPE);
        model.addAttribute("URL_PRODUCT_UPLOAD_DESIGN", URL_PRODUCT_UPLOAD_PROTOTYPE);
        model.addAttribute("URL_PRODUCT_REMOVE_DESIGN", URL_PRODUCT_REMOVE_PROTOTYPE);
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
