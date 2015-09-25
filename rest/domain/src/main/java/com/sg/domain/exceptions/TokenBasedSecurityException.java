/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.exceptions;

/**
 *
 * @author Admin
 */
public class TokenBasedSecurityException extends RuntimeException {

    public TokenBasedSecurityException(Exception cause) {
        super(cause);
    }

    public TokenBasedSecurityException() {
    }
    
    public TokenBasedSecurityException(String message)
    {
        super(message);
    }
}
