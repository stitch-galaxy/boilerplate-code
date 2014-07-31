/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.stitchgalaxy.sg_rest_api.configuration;

/**
 *
 * @author tarasev
 */

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
 
//TODO: get rid of this class
//http://www.mkyong.com/spring-security/spring-security-hello-world-annotation-example/
public class SpringMvcInitializer 
       extends AbstractAnnotationConfigDispatcherServletInitializer {
 
	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[] { WebConfig.class };
	}
 
	@Override
	protected Class<?>[] getServletConfigClasses() {
		return null;
	}
 
	@Override
	protected String[] getServletMappings() {
		return new String[] { "/*" };
	}
 
}
