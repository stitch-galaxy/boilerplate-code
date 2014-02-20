/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.stitchgalaxy.sg_manager_web;

import com.stitchgalaxy.service.DomainDataService;
import javax.servlet.Servlet;
//import org.springframework.web.context.WebApplicationContext;
//import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 *
 * @author tarasev
 */
public class DomainDataServiceUtils {
    
    public static DomainDataService getDomainDataService(Servlet servlet)
    {
//        WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(servlet.getServletConfig().getServletContext());
//        DomainDataService service = (DomainDataService) ctx.getBean("dataservice");
//        return service;
        return null;
    }
    
}
