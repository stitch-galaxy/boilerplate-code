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
import com.sg.sg_rest_api.security.UnauthorizedEntryPoint;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

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
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling().authenticationEntryPoint(unauthorizedEntryPoint())
                .and()
                .httpBasic()
                .and()
                .authorizeRequests()
                .antMatchers("/rest/thread/create").hasRole("ADMIN")
                .antMatchers("/**").permitAll()
                .anyRequest().hasRole("ADMIN");
    }
    
    @Bean
    public UnauthorizedEntryPoint unauthorizedEntryPoint()
    {
        UnauthorizedEntryPoint ep = new UnauthorizedEntryPoint();
        ep.setRealmName("Unauthorized access");
        return ep;
    }

}
