/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.rest.api.security;

import com.sg.rest.api.LoginController;
import com.sg.rest.api.ResendVerificationEmailController;
import com.sg.rest.api.SignupController;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

/**
 *
 * @author Admin
 */
@Configuration
@EnableWebMvcSecurity
@ComponentScan
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static void configureStatelessSecurityWithoutCsrfProtection(
            HttpSecurity http) throws Exception {
        //Stateless
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        //No csrf
        http.csrf().disable();
    }

    @Configuration
    @Order(1)
    public static class GetTokenSecurityContextConfiguration extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http = http
                    .antMatcher(LoginController.URI)
                    .antMatcher(SignupController.URI)
                    .antMatcher(ResendVerificationEmailController.URI);
            configureStatelessSecurityWithoutCsrfProtection(http);

            http.authorizeRequests().antMatchers("/**").permitAll()
                    .and()
                    .requiresChannel().anyRequest().requiresSecure();
        }
    }

    @Configuration
    @Order(2)
    public static class TokenBasedSecurityContextConfiguration extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http = http.antMatcher("/**");
            configureStatelessSecurityWithoutCsrfProtection(http);
            http.authorizeRequests().antMatchers("/**").permitAll();
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
