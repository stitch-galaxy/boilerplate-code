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
import com.sg.sg_rest_api.controllers.RequestPath;
import com.sg.sg_rest_api.security.AuthenticationTokenProcessingFilter;
import com.sg.sg_rest_api.security.Roles;
import com.sg.sg_rest_api.security.Security;
import com.sg.sg_rest_api.security.UnauthorizedEntryPoint;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityContext extends WebSecurityConfigurerAdapter {
        
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .addFilterBefore(authenticationTokenProcessingFilter(), DefaultLoginPageGeneratingFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling().defaultAuthenticationEntryPointFor(unauthorizedEntryPoint(), AnyRequestMatcher.INSTANCE)
                .and()
                .authorizeRequests()
                .antMatchers(RequestPath.REST_SECURE_ADMIN_PATH + "/**").hasRole(Roles.ROLE_ADMIN)
                .antMatchers(RequestPath.REST_SECURE_USER_PATH + "/**").hasRole(Roles.ROLE_USER)
                .anyRequest().permitAll();
    }
    
    @Bean
    public UnauthorizedEntryPoint unauthorizedEntryPoint()
    {
        UnauthorizedEntryPoint ep = new UnauthorizedEntryPoint();
        ep.setRealmName(UnauthorizedEntryPoint.REALM_NAME);
        return ep;
    }
    
    @Bean 
    public AuthenticationTokenProcessingFilter authenticationTokenProcessingFilter()
    {
        return new AuthenticationTokenProcessingFilter();
        
    }

}
