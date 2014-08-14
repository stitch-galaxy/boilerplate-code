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
public class SgSecurityException extends Exception {

    public SgSecurityException(Throwable cause) {
        super(cause);
    }
    
    public SgSecurityException(String message) {
        super(message);
    }
    
    public SgSecurityException(String message, Throwable cause) {
        super(message, cause);
    }
}