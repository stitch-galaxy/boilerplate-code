/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.rest.service.websecurity;

/**
 *
 * @author tarasev
 */
public class WebSecurityTokenExpiredException extends WebSecurityBaseAuthenticationException {

    public WebSecurityTokenExpiredException(Throwable t) {
        super("Token expired", t);
    }

}
