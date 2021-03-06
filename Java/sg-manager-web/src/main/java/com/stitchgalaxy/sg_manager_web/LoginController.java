/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stitchgalaxy.sg_manager_web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginController {

    @RequestMapping(value = UrlConstants.URL_LOGIN, method = RequestMethod.GET)
    public String login(ModelMap model) {

        UrlConstants.AddUrlConstants(model);
        
        return "login";

    }

    @RequestMapping(value = UrlConstants.URL_LOGIN_FAILED, method = RequestMethod.GET)
    public String loginerror(ModelMap model) {

        model.addAttribute("error", "true");
        UrlConstants.AddUrlConstants(model);

        return "login";

    }
}
