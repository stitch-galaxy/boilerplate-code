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
public class SgServiceLayerRuntimeException extends RuntimeException {

    public SgServiceLayerRuntimeException(Throwable cause) {
        super(cause);
    }
    
    public SgServiceLayerRuntimeException(String message) {
        super(message);
    }
    
    public SgServiceLayerRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
