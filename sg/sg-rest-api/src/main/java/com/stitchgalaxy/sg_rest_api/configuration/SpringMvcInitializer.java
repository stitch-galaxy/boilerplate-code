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
//http://www.luckyryan.com/2013/02/07/migrate-spring-mvc-servlet-xml-to-java-config/
//http://www.robinhowlett.com/blog/2013/02/13/spring-app-migration-from-xml-to-java-based-config/
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
