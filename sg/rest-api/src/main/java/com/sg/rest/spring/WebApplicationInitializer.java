/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.rest.spring;

import com.sg.rest.filters.CorsFilter;
import javax.servlet.ServletException;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;

/**
 *
 * @author tarasev
 */
public class WebApplicationInitializer extends
        AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{WebApplicationContext.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{SpringServletContextConfiguration.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        FilterRegistration.Dynamic corsFilter = servletContext.addFilter("corsFilter", CorsFilter.class);
        corsFilter.addMappingForUrlPatterns(null, true, "/*");
        super.onStartup(servletContext);
    }

}
