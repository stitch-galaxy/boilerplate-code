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
public class TokenBasedSecurityBadTokenException extends TokenBasedSecurityException {

    public TokenBasedSecurityBadTokenException(Exception cause) {
        super(cause);
    }
}
