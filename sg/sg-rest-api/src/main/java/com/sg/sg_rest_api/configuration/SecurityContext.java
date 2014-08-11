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
import com.sg.sg_rest_api.controllers.CustomUserDetailsService;
import com.sg.sg_rest_api.security.AuthenticationTokenProcessingFilter;
import com.sg.sg_rest_api.security.UnauthorizedEntryPoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityContext extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .addFilterBefore(authenticationTokenProcessingFilter(), DefaultLoginPageGeneratingFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling().defaultAuthenticationEntryPointFor(unauthorizedEntryPoint(), AnyRequestMatcher.INSTANCE)
                .and()
                //.and()
                //.httpBasic()
                .authorizeRequests()
                .antMatchers("/rest/thread/create").hasRole("ADMIN")
                .antMatchers("/**").permitAll();
        
    }
    
    @Bean
    public UnauthorizedEntryPoint unauthorizedEntryPoint()
    {
        UnauthorizedEntryPoint ep = new UnauthorizedEntryPoint();
        ep.setRealmName("Stitch galaxy");
        return ep;
    }
    
    @Bean 
    public AuthenticationTokenProcessingFilter authenticationTokenProcessingFilter()
    {
        return new AuthenticationTokenProcessingFilter();
        
    }

}
