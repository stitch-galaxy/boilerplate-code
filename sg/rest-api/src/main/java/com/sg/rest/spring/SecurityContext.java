/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.rest.spring;

/**
 *
 * @author tarasev
 */
import com.sg.domain.enumerations.Role;
import com.sg.rest.path.RequestPath;
import com.sg.rest.security.components.WebTokenProcessingFilter;
import com.sg.rest.security.components.NoOpClass;
import com.sg.rest.security.components.SgAccessDeniedHandler;
import com.sg.rest.security.components.WebTokenAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.ExceptionTranslationFilter;

@Configuration
@EnableWebMvcSecurity
@ComponentScan(basePackageClasses = {NoOpClass.class})
public class SecurityContext extends WebSecurityConfigurerAdapter {

    public static void ConfigureStatelessSecurityWithoutCsrfProtection(HttpSecurity http) throws Exception {
        //Stateless
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        //No csrf
        http.csrf().disable();
    }

    @Configuration
    @Order(1)
    public static class TokenBasedSecurityContextConfiguration extends WebSecurityConfigurerAdapter {

        @Autowired
        private WebTokenProcessingFilter tokenProcessingFilter;

        @Autowired
        private WebTokenAuthenticationEntryPoint authenticationFailedHandler;

        @Autowired
        private SgAccessDeniedHandler authorizationFailedHandler;

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http = http.antMatcher(RequestPath.REST_PATH + "/**");
            ConfigureStatelessSecurityWithoutCsrfProtection(http);

            //TODO: configure filter chain
            //http://stackoverflow.com/questions/13569303/handle-custom-exceptions-in-spring-security
            //http://stackoverflow.com/questions/10013996/referencing-spring-security-configuration-within-spring-3-1-java-config
            //http://shazsterblog.blogspot.ru/2014/07/spring-security-custom-filterchainproxy.html
            http.addFilterAfter(tokenProcessingFilter, ExceptionTranslationFilter.class);

            http.exceptionHandling().authenticationEntryPoint(authenticationFailedHandler);
            http.exceptionHandling().accessDeniedHandler(authorizationFailedHandler);

            //Authentication and authorization rules
            http.authorizeRequests()
                    .antMatchers(RequestPath.REST_SECURE_ADMIN_PATH + "/**").hasRole(Role.ADMIN.name())
                    .antMatchers(RequestPath.REST_SECURE_USER_PATH + "/**").hasRole(Role.USER.name())
                    .antMatchers(RequestPath.REST_USER_API_PATH + "/**").permitAll();
        }
    }

    @Configuration
    @Order(2)
    public static class DenyAccessSecurityContextConfiguration extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .authorizeRequests()
                    .anyRequest().permitAll();
        }
    }

}
