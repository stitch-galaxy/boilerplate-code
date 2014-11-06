/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.sg_rest_api.configuration;

/**
 *
 * @author tarasev
 */
import com.sg.constants.RequestPath;
import com.sg.rest.security.components.WebTokenProcessingFilter;
import com.sg.constants.Roles;
import com.sg.rest.security.components.NoOpClass;
import com.sg.rest.security.components.WebTokenAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter;

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

//        @Autowired
//        private CustomTokenBasedAccessDeniedHandler authorizationFailedHandler;
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http = http.antMatcher(RequestPath.REST_SECURE_PATH + "/**");
            ConfigureStatelessSecurityWithoutCsrfProtection(http);

            http.addFilterBefore(tokenProcessingFilter, DefaultLoginPageGeneratingFilter.class);

            http.exceptionHandling().authenticationEntryPoint(authenticationFailedHandler);
            //http.exceptionHandling().accessDeniedHandler(authorizationFailedHandler);

            //Authentication and authorization rules
            http.authorizeRequests()
                    .antMatchers(RequestPath.REST_SECURE_ADMIN_PATH + "/**").hasRole(Roles.ROLE_ADMIN)
                    .antMatchers(RequestPath.REST_SECURE_USER_PATH + "/**").hasRole(Roles.ROLE_USER)
                    .anyRequest().authenticated();
        }
    }

    @Configuration
    @Order(2)
    public static class PublicSecurityContextConfiguration extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http = http.antMatcher(RequestPath.REST_USER_API_PATH + "/**");
            
            ConfigureStatelessSecurityWithoutCsrfProtection(http);
            
            http
                    .authorizeRequests()
                    .anyRequest().permitAll();
        }
    }
    
    @Configuration
    @Order(3)
    public static class DenyAccessSecurityContextConfiguration extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .authorizeRequests()
                    .anyRequest().denyAll();
        }
    }

}
