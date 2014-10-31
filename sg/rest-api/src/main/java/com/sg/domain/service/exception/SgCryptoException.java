/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.domain.service.exception;

/**
 *
 * @author tarasev
 */
public class SgCryptoException extends Exception {

    public SgCryptoException(Throwable cause) {
        super(cause);
    }
    
    public SgCryptoException(String message) {
        super(message);
    }
    
    public SgCryptoException(String message, Throwable cause) {
        super(message, cause);
    }
}