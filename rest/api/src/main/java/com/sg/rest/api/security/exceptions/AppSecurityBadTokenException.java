/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.rest.api.security.exceptions;

/**
 *
 * @author Admin
 */
public class AppSecurityBadTokenException extends AppSecurityException {

    public AppSecurityBadTokenException(Exception cause) {
        super(cause);
    }
}
