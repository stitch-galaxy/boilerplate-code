/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.rest.security.components;

/**
 *
 * @author tarasev
 */
import com.sg.rest.enumerations.HttpCustomHeaders;
import com.sg.domain.operation.OperationExecutor;
import com.sg.dto.operation.status.GetAccountRolesStatus;
import com.sg.dto.operation.GetAccountRolesOperation;
import com.sg.dto.operation.response.GetAccountRolesResponse;
import com.sg.rest.security.SgRestUser;
import com.sg.rest.webtoken.WebSecurityAccountNotFoundException;
import com.sg.rest.webtoken.WebTokenService;
import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

@Component
public class WebTokenProcessingFilter extends GenericFilterBean {

    @Autowired
    private WebTokenService securityService;

    @Autowired
    OperationExecutor<GetAccountRolesResponse, GetAccountRolesOperation> handler;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest httpRequest = this.getAsHttpRequest(request);

        String sToken = this.extractAuthTokenFromRequest(httpRequest);
        if (sToken != null) {

            long accountId = securityService.getAccountIdAndVerifyToken(sToken);
            GetAccountRolesOperation dto = new GetAccountRolesOperation(accountId);
                GetAccountRolesResponse rolesDto = handler.handle(dto);
                if (rolesDto.getStatus() == GetAccountRolesStatus.STATUS_ACCOUNT_NOT_FOUND)
                {
                    throw new WebSecurityAccountNotFoundException(accountId);
                }
                
                SgRestUser userPrincipal = new SgRestUser(accountId);
                userPrincipal.setRoles(rolesDto.getData().getRoles());

                UsernamePasswordAuthenticationToken authentication
                        = new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
                SecurityContextHolder.getContext().setAuthentication(authentication);
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
        return httpRequest.getHeader(HttpCustomHeaders.AUTH_TOKEN_HEADER.getHeader());
    }
}
