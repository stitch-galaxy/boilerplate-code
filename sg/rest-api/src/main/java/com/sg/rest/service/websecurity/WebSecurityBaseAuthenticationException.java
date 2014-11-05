/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.rest.service.websecurity;

import org.springframework.security.core.AuthenticationException;

/**
 *
 * @author tarasev
 */
public class WebSecurityBaseAuthenticationException extends AuthenticationException {

    public WebSecurityBaseAuthenticationException(String message, Throwable t) {
        super(message, t);
    }

}
