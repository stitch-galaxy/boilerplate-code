/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.sg_rest_api.configuration;

import com.sg.domain.spring.configuration.JacksonMapperContext;
import com.sg.domain.spring.configuration.WebSecurityServiceContextConfiguration;
import com.sg.domain.spring.configuration.SgMailServiceContext;
import com.sg.domain.spring.configuration.JpaContext;
import com.sg.domain.spring.configuration.JpaServiceContext;
import com.sg.domain.spring.configuration.MapperContext;
import com.sg.domain.spring.configuration.PropertiesContextConfiguration;
import com.sg.domain.spring.configuration.ValidatorContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 *
 * @author tarasev
 */
//http://www.mkyong.com/spring-security/spring-security-hello-world-annotation-example/
//http://www.luckyryan.com/2013/02/07/migrate-spring-mvc-servlet-xml-to-java-config/
//http://www.robinhowlett.com/blog/2013/02/13/spring-app-migration-from-xml-to-java-based-config/
@Configuration
@EnableWebMvc
@Import({PropertiesContextConfiguration.class, ValidatorContext.class, JpaContext.class, MapperContext.class, JpaServiceContext.class, WebSecurityServiceContextConfiguration.class, JacksonMapperContext.class, SecurityContext.class, SgMailServiceContext.class})
public class WebApplicationContext extends WebMvcConfigurerAdapter {

    //https://jira.spring.io/browse/SPR-6443
    //http://stackoverflow.com/questions/3616359/who-sets-response-content-type-in-spring-mvc-responsebody
//    //http://stackoverflow.com/questions/3616359/who-sets-response-content-type-in-spring-mvc-responsebody/3617594#3617594
//    @Override
//    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//        StringHttpMessageConverter stringConverter = new StringHttpMessageConverter();
//        stringConverter.setSupportedMediaTypes(Arrays.asList(new MediaType("application", "json", StandardCharsets.UTF_8)));
//        converters.add(stringConverter);
//    }
}
