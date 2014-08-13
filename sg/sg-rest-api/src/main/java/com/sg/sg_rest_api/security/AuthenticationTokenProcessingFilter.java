/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.sg_rest_api.security;

/**
 *
 * @author tarasev
 */
import com.sg.enumerations.CustomHttpHeaders;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

@Component
public class AuthenticationTokenProcessingFilter extends GenericFilterBean {

    @Autowired
    Security security;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest httpRequest = this.getAsHttpRequest(request);

        String encryptedAuthToken = this.extractAuthTokenFromRequest(httpRequest);
        if (encryptedAuthToken != null) {
            try {
                AuthToken authToken = security.getTokenFromString(encryptedAuthToken);
                //TODO: check expiration
                
                UserDetails userDetails = createUserDetails(authToken);
                
                UsernamePasswordAuthenticationToken authentication
                        = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
            }
        }

        chain.doFilter(request, response);
    }

    private HttpServletRequest getAsHttpRequest(ServletRequest request) {
        if (!(request instanceof HttpServletRequest)) {
            throw new RuntimeException("Expecting an HTTP request");
        }

        return (HttpServletRequest) request;
    }

    private String extractAuthTokenFromRequest(HttpServletRequest httpRequest) {
        /* Get token from header */
        String authToken = httpRequest.getHeader(CustomHttpHeaders.X_AUTH_TOKEN);

        /* If token not found get it from request parameter */
        if (authToken == null) {
            authToken = httpRequest.getParameter("token");
        }

        return authToken;
    }
    

    private UserDetails createUserDetails(AuthToken authToken) {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        for (String a : authToken.getAuthorities()) {
            authorities.add(new SimpleGrantedAuthority(a));
        }

        return new User(
                authToken.getEmail(),
                "",
                authorities);
    }
}
